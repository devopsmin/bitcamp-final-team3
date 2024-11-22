package project.tripMaker.controller;

import java.util.HashMap;
import java.util.UUID;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import project.tripMaker.service.MailService;
import project.tripMaker.service.SMSService;
import project.tripMaker.service.StorageService;
import project.tripMaker.service.UserService;
import project.tripMaker.vo.User;

@RequiredArgsConstructor
@Controller
@RequestMapping("/auth")
public class AuthController {

  private final UserService userService;
  private final SMSService smsService;
  private final StorageService storageService;
  private final MailService mailService;
  private final PasswordEncoder passwordEncoder;

  private final String folderName = "user/profile/";

  @GetMapping("/login/form")
  public String form(
      @CookieValue(name = "userEmail", required = false) String userEmail,
      @RequestParam(value = "error", required = false) String error,
      HttpSession session,
      Model model) {

    if (userEmail != null && !userEmail.isEmpty()) {
      model.addAttribute("userEmail", userEmail);
    }

    if (error != null && session.getAttribute("loginError") != null) {
      model.addAttribute("errorMessage", session.getAttribute("loginError"));
      session.removeAttribute("loginError");
    }

    return "auth/login/form";
  }

//  @PostMapping("login")
//  public String login(
//      @RequestParam String userEmail,
//      @RequestParam String userPassword,
//      @RequestParam(required = false) boolean saveEmail,
//      HttpServletResponse res,
//      HttpSession session) throws Exception {
//
//    User user = userService.exists(userEmail, userPassword);
//    if (user == null) {
//      res.setHeader("Refresh", "2; url=login/form");
//      return "auth/login/fail";
//    }
//
//    userService.updateLastLogin(user.getUserNo());
//
//    if (saveEmail) {
//      Cookie cookie = new Cookie("userEmail", userEmail);
//      cookie.setMaxAge(60 * 60 * 24 * 7);
//      res.addCookie(cookie);
//    } else {
//      Cookie cookie = new Cookie("userEmail", "test@test.com");
//      cookie.setMaxAge(0);
//      res.addCookie(cookie);
//    }
//
//    session.setAttribute("loginUser", user);
//    return "redirect:/";
//  }

  @GetMapping("logout")
  public String logout(HttpSession session) {
    session.invalidate();
    return "redirect:/";
  }

  @GetMapping("/oauth2/callback")
  public String oauth2Callback(OAuth2AuthenticationToken authentication) {
    OAuth2User oauth2User = authentication.getPrincipal();
    String email = oauth2User.getAttribute("email");
    return "redirect:/home";
  }

  @GetMapping("/register/user")
  public String registerUserForm() {
    return "auth/register/user";
  }

  @PostMapping("/register/user")
  public String registerUser(
      User user,
      @RequestParam String confirmPassword,
      Model model,
      @RequestParam(required = false) MultipartFile file) {
    try {
      if (!user.getUserPassword().equals(confirmPassword)) {
        throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
      }

      if (!smsService.isPhoneVerified(user.getUserTel())) {
        throw new IllegalArgumentException("전화번호 인증이 필요합니다.");
      }

      // 프로필 이미지 처리
      if (file != null && !file.isEmpty()) {
        String filename = UUID.randomUUID().toString();
        HashMap<String, Object> options = new HashMap<>();
        options.put(StorageService.CONTENT_TYPE, file.getContentType());
        storageService.upload(folderName + filename,
            file.getInputStream(),
            options);
        user.setUserPhoto(filename);
      }

      userService.add(user);
      return "redirect:/";
    } catch (Exception e) {
      model.addAttribute("errorMessage", e.getMessage());
      return "auth/register/user";
    }
  }

  @GetMapping("/register/admin")
  public String registerAdminForm() {
    return "auth/register/admin";
  }

  @PostMapping("/register/admin")
  public String registerAdmin(User user, @RequestParam String confirmPassword, Model model) {
    try {
      if (!user.getUserPassword().equals(confirmPassword)) {
        model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
        return "auth/register/admin";
      }
      userService.addAdmin(user);
      return "redirect:/";
    } catch (Exception e) {
      model.addAttribute("errorMessage", "관리자 등록 중 오류가 발생했습니다.");
      return "auth/register/admin";
    }
  }

  @PostMapping("/find/password")
  @ResponseBody
  public String findPassword(@RequestParam String userEmail) throws Exception {
    User user = userService.getByEmail(userEmail);
    if (user == null) {
      return "존재하지 않는 이메일입니다.";
    }

    int verificationCode = mailService.sendMail(userEmail);
    return String.valueOf(verificationCode);
  }

  @PostMapping("/reset/password")
  @ResponseBody
  public String resetPassword(
      @RequestParam String userEmail,
      @RequestParam String userTel,
      @RequestParam String newPassword) throws Exception {

    User user = userService.getByEmail(userEmail);
    if (user == null) {
      return "존재하지 않는 이메일입니다.";
    }

    if (!user.getUserTel().equals(userTel)) {
      return "이메일과 전화번호가 일치하지 않습니다.";
    }

    String encodedPassword = passwordEncoder.encode(newPassword);
    user.setUserPassword(encodedPassword);

    if (userService.update(user)) {
      return "success";
    } else {
      return "비밀번호 변경에 실패했습니다.";
    }
  }

  @PostMapping("/find/id")
  @ResponseBody
  public String findId(@RequestParam String userTel) throws Exception {
    User user = userService.findByTel(userTel);
    if (user == null) {
      return "존재하지 않는 회원입니다.";
    }

    String email = user.getUserEmail();
    //    String maskedEmail = maskEmail(email);
    return "회원님의 아이디는 " + email + " 입니다.";
    //    return "회원님의 아이디는 " + maskedEmail + " 입니다.";
  }

  // 아이디 별표 쳐서 보여줌
  //  private String maskEmail(String email) {
  //    int atIndex = email.indexOf('@');
  //    if (atIndex <= 1) return email;
  //
  //    String local = email.substring(0, atIndex);
  //    String domain = email.substring(atIndex);
  //
  //    int visibleLength = Math.min(3, local.length());
  //    String visiblePart = local.substring(0, visibleLength);
  //    String maskedPart = "*".repeat(local.length() - visibleLength);
  //
  //    return visiblePart + maskedPart + domain;
  //  }

}
