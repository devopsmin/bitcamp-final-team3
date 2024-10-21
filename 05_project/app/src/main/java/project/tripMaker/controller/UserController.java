package project.tripMaker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.tripMaker.service.UserService;
import project.tripMaker.user.SignupForm;
import project.tripMaker.vo.User;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody Map<String, String> loginForm) throws Exception {
    String email = loginForm.get("email");
    String password = loginForm.get("password");

    String token = userService.login(email, password);
    return ResponseEntity.ok(token);
  }

  @GetMapping("/login")
  public String loginPage() {
    return "login";
  }

  @PostMapping("/signup")
  public ResponseEntity<Long> signup(@RequestBody SignupForm signupForm) throws Exception {
    User user = signupForm.toEntity();
    Long userId = userService.signup(user);
    return ResponseEntity.ok(userId);
  }

  @GetMapping("/signup")
  public String signupPage() {
    return "signup";
  }

}
