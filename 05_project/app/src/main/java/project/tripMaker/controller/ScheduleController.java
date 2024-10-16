package project.tripMaker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.tripMaker.service.ScheduleService;
import project.tripMaker.vo.City;
import project.tripMaker.vo.Location;
import project.tripMaker.vo.Schedule;
import project.tripMaker.vo.State;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/schedule")
@SessionAttributes({"location", "scheduleList"})

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
    List<State> stateList = scheduleService.stateList();
    model.addAttribute("stateList", stateList);
    return "schedule/form1";
  }

  @PostMapping("form2")
  public String form2(String stateCode, Model model) throws Exception {
    List<City> cityList = scheduleService.cityList(stateCode);
    model.addAttribute("cityList", cityList);
    return "schedule/form2";
  }

  @PostMapping("form3")
  public String form3(Model model, Location location, String cityCode) throws Exception {
    location.setCityCode(cityCode);
    model.addAttribute("location", location);
    return "schedule/form3";
  }

  @PostMapping("form4")
  public String form4(@ModelAttribute Location location, Model model) throws Exception {
    String cityCode = location.getCityCode();
    List<Location> locationList = scheduleService.locationList(cityCode);
    model.addAttribute("locationList", locationList);
    return "schedule/form4";
  }

  @PostMapping("form5")
  public String form5(@ModelAttribute Location location, int[] locationNos, Model model) throws Exception {
    List<Schedule> scheduleList = new ArrayList<>();
    for(int locationNo : locationNos) {
      Schedule schedule = new Schedule();
      schedule.setLocationNo(locationNo);
      scheduleService.addSchedule(schedule);
      scheduleList.add(schedule);
    }
    model.addAttribute("scheduleList", scheduleList);
    String cityCode = location.getCityCode();
    List<Location> hotelList = scheduleService.hotelList(cityCode);
    model.addAttribute("hotelList", hotelList);
    return "schedule/form5";
  }

  @PostMapping("form6")
  public String form6(@ModelAttribute List<Schedule> scheduleList, int[] hotelNos, Model model) throws Exception {
    for(int hotelNo : hotelNos) {
      Schedule schedule = new Schedule();
      schedule.setLocationNo(hotelNo);
      scheduleService.addSchedule(schedule);
    }
    model.addAttribute("hotelNos", hotelNos);

    List<Location> selectedLocation = new ArrayList<>();
    for (Schedule schedule : scheduleList) {
      int locationNo = schedule.getLocationNo();
      Location location = scheduleService.findLocation(locationNo);
      selectedLocation.add(location);
    }
    List<Location> selectedHotels = new ArrayList<>();
    for (int hotelNo : hotelNos) {
      Location location = scheduleService.findLocation(hotelNo);
      selectedHotels.add(location);
    }
    model.addAttribute("selectedLocation", selectedLocation);
    model.addAttribute("selectedHotels", selectedHotels);
    return "schedule/form6";
  }
}
