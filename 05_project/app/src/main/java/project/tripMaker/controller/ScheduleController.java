package project.tripMaker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.tripMaker.service.CityService;
import project.tripMaker.service.ScheduleService;
import project.tripMaker.vo.*;

import java.util.*;

@Controller
@RequestMapping("/schedule")
@SessionAttributes({"location", "locationNos", "trip"})

public class ScheduleController {

  private CityService cityService;
  private ScheduleService scheduleService;

  public ScheduleController(
      ScheduleService scheduleService, CityService cityService) {
    this.scheduleService = scheduleService;
    this.cityService = cityService;
  }

  @GetMapping("main")
  public void list() throws Exception {
  }

  @GetMapping("form1")
  public void form1(Model model, Trip trip) throws Exception {
    List<State> stateList = cityService.stateList();
    scheduleService.makeTrip(trip);
    model.addAttribute("stateList", stateList);
    model.addAttribute("trip", trip);
  }

  @PostMapping("form2")
  public void form2(String stateCode, Model model) throws Exception {
    List<City> cityList = cityService.cityList(stateCode);
    model.addAttribute("cityList", cityList);
  }

  @PostMapping("form3")
  public void form3(@ModelAttribute Trip trip, String cityCode,Model model) throws Exception {
    City city = cityService.firndCity(cityCode);
    trip.setCity(city);
  }

  @PostMapping("form4")
  public void form4(@ModelAttribute Trip trip, Model model) throws Exception {
    String cityCode = trip.getCity().getCityCode();
    List<Location> locationList = scheduleService.locationList(cityCode);
    model.addAttribute("locationList", locationList);
    scheduleService.updateTrip(trip);
  }

  @PostMapping("form5")
  public void form5(@ModelAttribute Trip trip, int[] locationNos, Model model) throws Exception {
    model.addAttribute("locationNos", locationNos);

    String cityCode = trip.getCity().getCityCode();
    List<Location> hotelList = scheduleService.hotelList(cityCode);
    model.addAttribute("hotelList", hotelList);
  }

  @PostMapping("form6")
  public void form6(@ModelAttribute("locationNos") List<Integer> locationNos, int[] hotelNos, Model model) throws Exception {

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
  }

  @PostMapping("form7")
  public void form7(@ModelAttribute Trip trip, Model model) throws Exception {
    for(Schedule schedule : trip.getScheduleList()) {
      schedule.setTripNo(trip.getTripNo());
      scheduleService.addSchedule(schedule);
    }
    List<Schedule> scheduleList = scheduleService.viewSchedule(trip.getTripNo());
    trip.setScheduleList(scheduleList);

    model.addAttribute("scheduleList", scheduleList);
  }

  @GetMapping("form8")
  public void form8(Model model) throws Exception {
    Map<Integer, String> thema = scheduleService.selectThema();
    model.addAttribute("themaMap", thema);
  }


}
//
//    request.getParameterMap().forEach((key, value) -> {
//      System.out.println("Key: " + key + ", Value: " + Arrays.toString(value));
//    });
