package project.tripMaker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.tripMaker.service.ScheduleService;

import java.util.List;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {

  private ScheduleService scheduleService;

  public ScheduleController(
      ScheduleService scheduleService) {
    this.scheduleService = scheduleService;

  }

  @GetMapping("main")
  public String list() throws Exception {
    return "schedule/main";
  }

  @GetMapping("form1")
  public String form1(Model model) throws Exception {
    List<String> cityList = scheduleService.cityList();
    for (String city : cityList) {
      System.out.println(city.toString());
    }
    model.addAttribute("cityList", cityList);
    return "schedule/form1";
  }
}
