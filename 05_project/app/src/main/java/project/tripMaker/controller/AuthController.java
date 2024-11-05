package project.tripMaker.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.tripMaker.service.MailService;
import project.tripMaker.service.UserService;
import project.tripMaker.vo.User;

@RequiredArgsConstructor
@Controller
@RequestMapping("/auth")
public class AuthController {

  private final UserService userService;
  private final MailService mailService;

  @GetMapping("form")
  public void form(@CookieValue(name = "userEmail", required = false) String userEmail, Model model) {
    model.addAttribute("userEmail", userEmail);
  }

  @PostMapping("login")
  public String login(
      String userEmail,
      String userPassword,
      boolean saveEmail,
      HttpServletResponse res,
      HttpSession session) throws Exception {

    User user = userService.exists(userEmail, userPassword);
    if (user == null) {
      res.setHeader("Refresh", "2; url=form");
      return "auth/fail";
    }

    userService.updateLastLogin(user.getUserNo());

    if (saveEmail) {
      Cookie cookie = new Cookie("userEmail", userEmail);
      cookie.setMaxAge(60 * 60 * 24 * 7);
      res.addCookie(cookie);
    } else {
      Cookie cookie = new Cookie("userEmail", "test@test.com");
      cookie.setMaxAge(0);
      res.addCookie(cookie);
    }

    session.setAttribute("loginUser", user);
    return "redirect:/";
  }

  @GetMapping("logout")
  public String logout(HttpSession session) {
    session.invalidate();
    return "redirect:/";
  }

  @GetMapping("registerUser")
  public String registerUserForm() {
    return "auth/registerUser";
  }

  @PostMapping("registerUser")
  public String registerUser(User user, @RequestParam String confirmPassword, Model model) {
    try {
      if (!user.getUserPassword().equals(confirmPassword)) {
        throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
      }

      userService.add(user);
      return "redirect:/";
    } catch (Exception e) {
      model.addAttribute("errorMessage", e.getMessage());
      return "auth/registerUser";
    }
  }

  @GetMapping("registerAdmin")
  public String registerAdminForm() {
    return "auth/registerAdmin";
  }

  @PostMapping("registerAdmin")
  public String registerAdmin(User user, @RequestParam String confirmPassword, Model model) {
    try {
      if (!user.getUserPassword().equals(confirmPassword)) {
        model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
        return "auth/registerAdmin";
      }
      userService.addAdmin(user);
      return "redirect:/";
    } catch (Exception e) {
      model.addAttribute("errorMessage", "관리자 등록 중 오류가 발생했습니다.");
      return "auth/registerAdmin";
    }
  }


  @GetMapping("findIdForm")
  public String showFindIdForm(Model model) {
    return "auth/findIdForm";
  }

  @PostMapping("findUserByTel")
  public String findUserByTel(@RequestParam String userTel, Model model) throws Exception {
    User user = userService.findByTel(userTel);

    if (user == null) {
      model.addAttribute("message", "존재하지 않는 유저입니다.");
      return "auth/findIdForm";
    } else {
      model.addAttribute("message", "아이디 : " + user.getUserEmail());
      return "auth/findIdResult";
    }
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

  @GetMapping("/oauth2/callback")
  public String oauth2Callback(OAuth2AuthenticationToken authentication) {
    OAuth2User oauth2User = authentication.getPrincipal();
    String email = oauth2User.getAttribute("email");
    return "redirect:/home";
  }


  @GetMapping("/phone-verification")
  public String showPhoneVerificationForm(@RequestParam String email, Model model) {
    model.addAttribute("email", email);
    return "auth/phoneVerification";
  }

  @PostMapping("/phone-verification")
  public String verifyPhone(@RequestParam String email,
      @RequestParam String phoneNumber,
      HttpSession session,
      Model model) {
    try {
      if (!phoneNumber.matches("\\d{3}-\\d{4}-\\d{4}")) {
        throw new IllegalArgumentException("올바른 전화번호 형식이 아닙니다. (예: 010-1234-5678)");
      }

      User existingUser = userService.findByTel(phoneNumber);
      if (existingUser != null) {
        throw new IllegalArgumentException("이미 등록된 전화번호입니다.");
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
      return "auth/phoneVerification";
    } catch (Exception e) {
      model.addAttribute("error", "전화번호 인증 중 오류가 발생했습니다.");
      return "auth/phoneVerification";
    }
  }

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


}
