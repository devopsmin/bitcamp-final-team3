package project.tripMaker.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import project.tripMaker.service.StorageService;
import project.tripMaker.service.UserService;
import project.tripMaker.vo.User;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {

  private final UserService userService;
  private final StorageService storageService;


  private final String folderName = "user/profile/";

  @GetMapping
  public String list(Model model) throws Exception {
    List<User> list = userService.list();
    model.addAttribute("list", list);
    return "admin/list";
  }

  @Transactional
  @DeleteMapping("/{userNo}")
  @ResponseBody
  public String delete(@PathVariable Long userNo) throws Exception {
    User old = userService.get(userNo);
    if (userService.delete(userNo)) {
      storageService.delete(folderName + old.getUserPhoto());
      return "success";
    } else {
      return "failure";
    }
  }
}
