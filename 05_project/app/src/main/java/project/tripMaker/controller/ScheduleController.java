package project.tripMaker.controller;

import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import project.tripMaker.service.CityService;
import project.tripMaker.service.ScheduleService;
import project.tripMaker.vo.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Controller
@RequestMapping("/schedule")
@SessionAttributes({"locationNos", "trip", "myLocations", "myHotels", "tripType"})
public class ScheduleController {

  private final CityService cityService;
  private final ScheduleService scheduleService;

  @RequestMapping("selectState")
  public void selectState(Model model, Trip trip) throws Exception {
    List<State> stateList = cityService.stateList();
    model.addAttribute("stateList", stateList);
    model.addAttribute("trip", trip);
    //    model.addAttribute("tripType", tripType);
  }

  @PostMapping("selectCity")
  @ResponseBody  // JSON 형태로 응답
  public List<City> selectCity(@RequestParam String stateCode) throws Exception {
    // stateCode에 해당하는 cityList를 가져옴
    List<City> cityList = cityService.cityList(stateCode);
    return cityList;  // JSON 형태로 반환
  }

  @PostMapping("selectDate")
  public void selectDate(@ModelAttribute Trip trip, String cityCode, String tripType, Model model)
      throws Exception {
    trip.setCity(cityService.firndCity(cityCode));
    model.addAttribute("tripType", tripType);
    model.addAttribute("myLocations", new ArrayList<>());
    model.addAttribute("myHotels", new ArrayList<>());
  }

  @PostMapping("selectLocation")
  public void selectLocation(
      @ModelAttribute Trip trip,
      @ModelAttribute("myLocations") List<Location> myLocations,
      @ModelAttribute("myLocation") Location myLocation,
      Model model) throws Exception {

    scheduleService.calculateDay(trip);
    String cityCode = trip.getCity().getCityCode();

    if (myLocation.getLocationName() != null && !myLocations.contains(myLocation)) {
      myLocation.setCityCode(trip.getCity().getCityCode());
      myLocations.add(myLocation);
      scheduleService.addLocation(myLocation);
      for (Location location : myLocations) {
        System.out.println(location.getLocationName());
      }
    }

    List<Location> locationList = scheduleService.locationList(cityCode);
    model.addAttribute("locationList", locationList);
    model.addAttribute("myLocations", myLocations);
  }

  @RequestMapping("addLocation")
  public void addLocation(Model model) throws Exception {
    Location myLocation = new Location();
    model.addAttribute("myLocation", myLocation);
  }

  @PostMapping("selectHotel")
  public void selectHotel(
      @ModelAttribute Trip trip,
      @ModelAttribute("myHotels") ArrayList<Location> myHotels,
      Location myHotel,
      int[] locationNos,
      Model model) throws Exception {
    model.addAttribute("locationNos", locationNos);
    if (myHotel.getLocationName() != null && !myHotels.contains(myHotel)) {
      myHotel.setCityCode(trip.getCity().getCityCode());
      myHotels.add(myHotel);
      scheduleService.addLocation(myHotel);
    }

    String cityCode = trip.getCity().getCityCode();
    List<Location> hotelList = scheduleService.hotelList(cityCode);
    model.addAttribute("hotelList", hotelList);
  }

  @RequestMapping("addHotel")
  public void addHotel(Model model) throws Exception {
    Location myHotel = new Location();
    model.addAttribute("myHotel", myHotel);
  }

  @PostMapping("createSchedule")
  public void createSchedule(
      @ModelAttribute("locationNos") List<Integer> locationNos,
      int[] hotelNos,
      Model model) throws Exception {

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

  @PostMapping("selectTripList")
  public void getTripList(@ModelAttribute Trip trip, Model model) throws Exception {
    List<Trip> tripList = scheduleService.getTripList(trip);
    model.addAttribute("tripList", tripList);
  }

  @PostMapping("editSchedule")
  public void editSchedule(
      @ModelAttribute Trip trip,
      Integer selectedTripNo,
      Model model) throws Exception {
    List<Schedule> scheduleList = scheduleService.viewSchedule(selectedTripNo);
    model.addAttribute("scheduleList", scheduleList);
  }

  @PostMapping("checkSchedule")
  public void checkSchedule(
      @ModelAttribute Trip trip,
      Model model) throws Exception {
    List<Schedule> scheduleList = scheduleService.orderSchedule(trip.getScheduleList());
    for (Schedule schedule : scheduleList) {
      schedule.setLocation(scheduleService.findLocation(schedule.getLocation().getLocationNo()));
    }
    model.addAttribute("scheduleList", scheduleList);
  }

  @PostMapping("saveTrip")
  public void saveTrip(Model model) throws Exception {
    List<Thema> themas = scheduleService.themaList();
    model.addAttribute("themas", themas);
  }

  @PostMapping("save")
  public void save(
      @ModelAttribute Trip trip,
      int themaNo,
      SessionStatus sessionStatus) throws Exception {
    trip.setUserNo((long)1);
    Thema thema = scheduleService.getThema(themaNo);
    trip.setThema(thema);
    System.out.println("==========================================");
    System.out.println(trip.toString());
    System.out.println("==========================================");
    scheduleService.saveTrip(trip);
    sessionStatus.setComplete();
  }
}
