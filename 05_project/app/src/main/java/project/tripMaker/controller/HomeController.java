package project.tripMaker.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  @GetMapping("/home")
  public void home(Model model) {
    model.addAttribute("welcome", "<b>안녕</b>하세요!");
  }
}
