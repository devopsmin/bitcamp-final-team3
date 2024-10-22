package project.tripMaker.controller;


import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import project.tripMaker.service.UserService;
import project.tripMaker.vo.User;

@RequiredArgsConstructor
@Controller
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  @GetMapping("form")
  public String form() {
    return "user/form";
  }

  @PostMapping
  public String add(User user, MultipartFile file) throws Exception {
    userService.add(user);
    return "redirect:../users";
  }

  @GetMapping
  public String list(Model model) throws Exception {
    List<User> list = userService.list();
    model.addAttribute("list", list);
    return "user/list";
  }

  @GetMapping("{userNo}")
  public String view(
          @PathVariable int userNo,
          Model model) throws Exception {
    User user = userService.get(userNo);
    model.addAttribute("user", user);
    return "user/view";
  }

  @PostMapping("{userNo}")
  public String update(
          @PathVariable int userNo,
          User user) throws Exception {
    user.setUserNo(userNo);
    if (userService.update(user)) {
      return "redirect:../users";
    } else {
      throw new Exception("없는 회원입니다!");
    }
  }

  @Transactional
  @DeleteMapping("{userNo}")
  @ResponseBody
  public String delete(@PathVariable int userNo) throws Exception {
    if (userService.delete(userNo)) {
      return "success";
    } else {
      return "failure";
    }
  }
}
