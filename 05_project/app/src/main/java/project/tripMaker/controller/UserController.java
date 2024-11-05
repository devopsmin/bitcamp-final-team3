package project.tripMaker.controller;

import java.util.List;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.tripMaker.service.UserService;
import project.tripMaker.vo.User;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  @GetMapping("form")
  public String form() {
    return "/user/form";
  }

  @PostMapping("add")
  public String add(User user) throws Exception {
    userService.add(user);
    return "redirect:../user";
  }

  @PostMapping("addAdmin")
  public String addAdmin(User user) throws Exception {
    userService.addAdmin(user);
    return "redirect:../user";
  }

  @GetMapping
  public String list(Model model) throws Exception {
    List<User> list = userService.list();
    model.addAttribute("list", list);
    return "/user/list";
  }

  @GetMapping("{userNo}")
  public String view(
      @PathVariable Long userNo,
      Model model) throws Exception {
    User user = userService.get(userNo);
    model.addAttribute("user", user);
    return "/user/view";
  }

  @GetMapping("myInfo")
  public String myInfo(
      HttpSession session,
      Model model) throws Exception {
    User loginUser = (User) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new Exception("로그인이 필요합니다.");
    }
    User user = userService.get(loginUser.getUserNo());
    model.addAttribute("user", user);
    return "/user/view";
  }

  @PostMapping("{userNo}")
  public String update(
      @PathVariable Long userNo,
      User user) throws Exception {
    user.setUserNo(userNo);
    if (userService.update(user)) {
      return "redirect:../user";
    } else {
      throw new Exception("없는 회원입니다!");
    }
  }

  @Transactional
  @DeleteMapping("{userNo}")
  @ResponseBody
  public String delete(@PathVariable Long userNo) throws Exception {
    if (userService.delete(userNo)) {
      return "success";
    } else {
      return "failure";
    }
  }
}
