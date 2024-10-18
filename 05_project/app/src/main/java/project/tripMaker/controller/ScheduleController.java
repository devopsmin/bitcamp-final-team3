package project.tripMaker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import project.tripMaker.service.ScheduleService;
import project.tripMaker.vo.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.beans.PropertyEditorSupport;
import java.util.*;

@Controller
@RequestMapping("/schedule")
@SessionAttributes({"location", "locationNos", "trip"})

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
  public String form1(Model model, Trip trip) throws Exception {
    List<State> stateList = scheduleService.stateList();
    scheduleService.makeTrip(trip);
    model.addAttribute("stateList", stateList);
    model.addAttribute("trip", trip);
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
    City city = scheduleService.firndCity(cityCode);
    location.setCity(city);
    model.addAttribute("location", location);
    return "schedule/form3";
  }

  @PostMapping("form4")
  public String form4(@ModelAttribute Location location, Model model,@ModelAttribute Trip trip) throws Exception {
    String cityCode = location.getCity().getCityCode();
    List<Location> locationList = scheduleService.locationList(cityCode);
    model.addAttribute("locationList", locationList);
    scheduleService.updateTrip(trip);
    return "schedule/form4";
  }

  @PostMapping("form5")
  public String form5(@ModelAttribute Location location, int[] locationNos, Model model) throws Exception {
    model.addAttribute("locationNos", locationNos);

    String cityCode = location.getCity().getCityCode();
    List<Location> hotelList = scheduleService.hotelList(cityCode);
    model.addAttribute("hotelList", hotelList);

    return "schedule/form5";
  }

  @PostMapping("form6")
  public String form6(@ModelAttribute("locationNos") List<Integer> locationNos, int[] hotelNos, Model model) throws Exception {

    List<Location> selectedLocation = new ArrayList<>();
    for (int locationNo : locationNos) {
      Location location = scheduleService.findLocation(locationNo);
      selectedLocation.add(location);
    }

    List<Location> selectedHotel = new ArrayList<>();
    for (int hotelNo : hotelNos) {
      Location location = scheduleService.findLocation(hotelNo);
      selectedHotel.add(location);
    }
    model.addAttribute("selectedLocation", selectedLocation);
    model.addAttribute("selectedHotels", selectedHotel);
    return "schedule/form6";
  }

  @PostMapping("form7")
  public String form7(@ModelAttribute Trip trip, Model model) throws Exception {
    for(Schedule schedule : trip.getScheduleList()) {
      schedule.setTripNo(trip.getTripNo());
      scheduleService.addSchedule(schedule);
    }
    List<Schedule> scheduleList = scheduleService.viewSchedule(trip.getTripNo());
    trip.setScheduleList(scheduleList);

    model.addAttribute("scheduleList", scheduleList);
    return "schedule/form7";
  }
}
//
//    request.getParameterMap().forEach((key, value) -> {
//      System.out.println("Key: " + key + ", Value: " + Arrays.toString(value));
//    });
