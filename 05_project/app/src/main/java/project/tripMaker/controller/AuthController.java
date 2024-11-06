package project.tripMaker.controller;

import java.util.HashMap;
import java.util.UUID;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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

  private final String folderName = "user/";

  @GetMapping("/login/form")
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
      res.setHeader("Refresh", "2; url=login/form");
      return "auth/login/fail";
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

  @GetMapping("/find/id")
  public String showFindIdForm(Model model) {
    return "auth/find/id-form";
  }

  @PostMapping("/find/id/result")
  public String findUserByTel(@RequestParam String userTel, Model model) throws Exception {
    User user = userService.findByTel(userTel);

    if (user == null) {
      model.addAttribute("message", "존재하지 않는 유저입니다.");
      return "auth/find/id-form";
    } else {
      model.addAttribute("message", "아이디 : " + user.getUserEmail());
      return "auth/find/id-result";
    }
  }
}
