package project.tripMaker.controller;

import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import project.tripMaker.service.CityService;
import project.tripMaker.service.ScheduleService;
import project.tripMaker.vo.*;

import java.sql.SQLOutput;
import java.util.*;

@Data
@Controller
@RequestMapping("/schedule")
@SessionAttributes({"locationNos", "trip", "myLocations", "myHotels", "tripType"})
public class ScheduleController {

  private final CityService cityService;
  private final ScheduleService scheduleService;

  @GetMapping("main")
  public void list() throws Exception {
  }

  @PostMapping("createTrip")
  public void form1(Model model, Trip trip, String tripType) throws Exception {
    List<State> stateList = cityService.stateList();
    model.addAttribute("stateList", stateList);
    model.addAttribute("trip", trip);
    model.addAttribute("tripType", tripType);
  }

  @PostMapping("selectCity")
  public void selectCity(String stateCode, Model model) throws Exception {
    List<City> cityList = cityService.cityList(stateCode);
    model.addAttribute("cityList", cityList);
  }

  @PostMapping("selectDate")
  public void selectDate(@ModelAttribute Trip trip, String cityCode, Model model) throws Exception {
    trip.setCity(cityService.firndCity(cityCode));
    List<Location> myLocations = new ArrayList<>();
    List<Location> myHotels = new ArrayList<>();
    model.addAttribute("myLocations", myLocations);
    model.addAttribute("myHotels", myHotels);
  }

  @PostMapping("selectLocation")
  public void selectLocation(
      @ModelAttribute Trip trip,
      @ModelAttribute("myLocations") ArrayList<Location> myLocations,
      @ModelAttribute("myLocation") Location myLocation,
      Model model) throws Exception {
    String cityCode = trip.getCity().getCityCode();

    if (myLocation.getLocationName() != null) {
      myLocation.setCityCode(trip.getCity().getCityCode());
      myLocations.add(myLocation);
      scheduleService.addLocation(myLocation);
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

    if (myHotel.getLocationName() != null) {
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
      Model model) throws Exception{
    List<Schedule> scheduleList = scheduleService.viewSchedule(selectedTripNo);
    model.addAttribute("scheduleList", scheduleList);
  }

  @PostMapping("checkSchedule")
  public void checkSchedule(
      @ModelAttribute Trip trip,
      Model model) throws Exception {

    scheduleService.makeTrip(trip);
    for(Schedule schedule : trip.getScheduleList()) {
      schedule.setTripNo(trip.getTripNo());
      scheduleService.addSchedule(schedule);
    }
    List<Schedule> scheduleList = scheduleService.viewSchedule(trip.getTripNo());
    trip.setScheduleList(scheduleList);

    model.addAttribute("scheduleList", scheduleList);
  }

  @GetMapping("saveTrip")
  public void saveTrip(Model model) throws Exception {
    List<Thema> themas = scheduleService.themaList();
    for(Thema thema : themas) {
      System.out.println(thema);
    }
    model.addAttribute("themas", themas);
  }

  @PostMapping("save")
  public void save(
      @ModelAttribute Trip trip,
      int themaNo,
      SessionStatus sessionStatus) throws Exception {
    trip.setUserNo(1);
    Thema thema = scheduleService.getThema(themaNo);
    trip.setThema(thema);
    scheduleService.saveTrip(trip);
    sessionStatus.setComplete();
  }
}
