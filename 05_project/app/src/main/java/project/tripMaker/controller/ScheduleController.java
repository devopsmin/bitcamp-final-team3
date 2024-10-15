package project.tripMaker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import project.tripMaker.service.ScheduleService;
import project.tripMaker.vo.City;
import project.tripMaker.vo.Location;
import project.tripMaker.vo.State;
import project.tripMaker.vo.Trip;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/schedule")
@SessionAttributes("location")

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
  public String form1(Location location, Model model) throws Exception {
    List<City> cityList = scheduleService.cityList();
    model.addAttribute("cityList", cityList);
    model.addAttribute("location", location);
    return "schedule/form1";
  }

  @PostMapping("form2")
  public String form2(@ModelAttribute Location location, String cityCode, Model model) throws Exception {
    List<State> stateList = scheduleService.stateList(cityCode);
    location.setCityCode(cityCode);
    model.addAttribute("stateList", stateList);
    return "schedule/form2";
  }

  @PostMapping("form3")
  public String form3(@ModelAttribute Location location, String stateCode) throws Exception {
    location.setStateCode(stateCode);
    return "schedule/form3";
  }

  @PostMapping("form4")
  public String form4(@ModelAttribute Location location, Model model) throws Exception {
    String stateCode = location.getStateCode();
    List<Location> locationList = scheduleService.locationList(stateCode);
    model.addAttribute("locationList", locationList);
    return "schedule/form4";
  }

  @PostMapping("form5")
  public String form5(@ModelAttribute Location location,Model model) throws Exception {
    String stateCode = location.getStateCode();
    List<Location> locationList = scheduleService.locationList(stateCode);
    model.addAttribute("locationList", locationList);
    return "schedule/form5";
  }
}
