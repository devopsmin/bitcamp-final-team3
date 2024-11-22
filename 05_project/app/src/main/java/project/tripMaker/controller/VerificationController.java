package project.tripMaker.controller;

import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import project.tripMaker.service.MailService;
import project.tripMaker.service.SMSService;
import project.tripMaker.service.UserService;
import project.tripMaker.vo.User;

@RequiredArgsConstructor
@Controller
@RequestMapping("/verify")
public class VerificationController {

  private final MailService mailService;
  private final SMSService smsService;
  private final UserService userService;

  @PostMapping("/mail-confirm")
  @ResponseBody
  public String mailConfirm(@RequestParam String email) {
    try {
      if (userService.existsByEmail(email)) {
        return "이미 가입된 이메일입니다.";
      }

      int confirmNumber = mailService.sendMail(email);
      return String.valueOf(confirmNumber);

    } catch (Exception e) {
      return "이메일 전송에 실패했습니다.";
    }
  }

  @PostMapping("/send-sms")
  @ResponseBody
  public String sendSMS(@RequestParam String phoneNumber) {
    try {
      String verificationCode = smsService.sendVerificationMessage(phoneNumber);
      return verificationCode;
    } catch (Exception e) {
      return "SMS 전송에 실패했습니다.";
    }
  }

  @PostMapping("/verify-sms")
  @ResponseBody
  public String verifySMS(
      @RequestParam String phoneNumber,
      @RequestParam String code) {
    boolean isValid = smsService.verifyCode(phoneNumber, code);
    return isValid ? "success" : "fail";
  }

  @PostMapping("/check-nickname")
  @ResponseBody
  public String checkNickname(@RequestParam String nickname) {
    try {
      if (userService.existsByNickname(nickname)) {
        return "duplicate";
      }
      return "available";
    } catch (Exception e) {
      return "error";
    }
  }

  @GetMapping("/phone")
  public String showPhoneVerificationForm(@RequestParam String email, Model model) {
    model.addAttribute("email", email);
    return "auth/verify/phone";
  }

  @PostMapping("/phone")
  public String verifyPhone(@RequestParam String email,
                            @RequestParam String phoneNumber,
                            HttpSession session,
                            Model model) {
    try {
      if (!phoneNumber.matches("\\d{3}-\\d{4}-\\d{4}")) {
        throw new IllegalArgumentException("올바른 전화번호 형식이 아닙니다. (예: 010-1234-5678)");
      }

      if (!smsService.isPhoneVerified(phoneNumber)) {
        throw new IllegalArgumentException("전화번호 인증이 필요합니다.");
      }

      User existingUser = userService.findByTel(phoneNumber);
      if (existingUser != null) {
        model.addAttribute("email", email);
        model.addAttribute("error", "이미 회원가입된 전화번호입니다.");
        return "auth/verify/phone";
      }

      User user = userService.getByEmail(email);
      user.setUserTel(phoneNumber);
      userService.update(user);

      session.setAttribute("loginUser", user);
      session.removeAttribute("tempLoginUser");

      return "redirect:/home";

    } catch (IllegalArgumentException e) {
      model.addAttribute("email", email);
      model.addAttribute("error", e.getMessage());
      return "auth/verify/phone";
    } catch (Exception e) {
      model.addAttribute("error", "전화번호 인증 중 오류가 발생했습니다.");
      return "auth/verify/phone";
    }
  }
}
