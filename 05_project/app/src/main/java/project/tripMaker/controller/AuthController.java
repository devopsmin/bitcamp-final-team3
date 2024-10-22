package project.tripMaker.controller;



import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.tripMaker.service.UserService;
import project.tripMaker.vo.User;


@RequiredArgsConstructor
@Controller
@RequestMapping("/auth")
public class AuthController {

  private final UserService userService;

  @GetMapping("form")
  public void form() {
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

    if (saveEmail) {
      Cookie cookie = new Cookie("email", userEmail);
      cookie.setMaxAge(60 * 60 * 24 * 7);
      res.addCookie(cookie);
    } else {
      Cookie cookie = new Cookie("email", "test@test.com");
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

}
