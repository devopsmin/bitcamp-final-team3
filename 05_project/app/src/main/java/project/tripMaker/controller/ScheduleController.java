package project.tripMaker.controller;

import com.fasterxml.jackson.databind.JsonNode;
import dto.LocationDTO;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import project.tripMaker.service.*;
import project.tripMaker.vo.*;

import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Controller
@RequestMapping("/schedule")
@SessionAttributes(
    {"locationNos", "trip", "tripType", "locationList", "selectLoList",
        "hotelList", "selectHoList", "tripScheduleList"})
public class ScheduleController {

  private final CityService cityService;
  private final ScheduleService scheduleService;
  private final TourAPIService tourAPIService;
  private final DirectionService directionService;
  private final RouteService routeService;
  private final SearchAPIService searchAPIService;

  @RequestMapping("selectState")
  public void selectState(Model model, Trip trip) throws Exception {
    List<State> stateList = cityService.stateList();
    model.addAttribute("stateList", stateList);
    model.addAttribute("trip", trip);

    List<Location> selectLoList = new ArrayList<>();
    model.addAttribute("selectLoList", selectLoList);
    List<Location> searchLoList = new ArrayList<>();
    model.addAttribute("searchLoList", searchLoList);

    //    model.addAttribute("tripType", tripType);
  }

  @PostMapping("selectCity")
  @ResponseBody
  public List<City> selectCity(@RequestBody String stateCode) throws Exception {

    return cityService.cityList(stateCode);
  }

  @PostMapping("selectDate")
  public void selectDate(
      @ModelAttribute Trip trip,
      String tripType,
      String cityCode,
      Model model)
      throws Exception {
    trip.setCity(cityService.firndCity(cityCode));

    List<Location> locationList =
        tourAPIService.showLocation(trip.getCity());



    model.addAttribute("needDateSelection", trip.getStartDate() == null);
    model.addAttribute("trip", trip);
    model.addAttribute("locationList", locationList);
  }

  @GetMapping("selectDate")
  public void selectDate(
      @ModelAttribute Trip trip,
      Model model)
      throws Exception {
    model.addAttribute("trip", trip);
  }


  @PostMapping("/saveDates")
  @ResponseBody
  public Trip saveDates(
      @RequestParam String startDate,
      @RequestParam String endDate,
      @ModelAttribute Trip trip,
      Model model) {

    Date sqlStartDate = Date.valueOf(startDate);
    Date sqlEndDate = Date.valueOf(endDate);

    trip.setStartDate(sqlStartDate);
    trip.setEndDate(sqlEndDate);
    scheduleService.calculateDay(trip);
    List<Location> selectHoList = Stream.generate(() -> (Location) null)
        .limit(trip.getTotalDay())
        .collect(Collectors.toList());
    model.addAttribute("selectHoList", selectHoList);
    return trip;  // Trip 객체를 직접 반환
  }

  @RequestMapping("selectLocation")
  public void selectLocation(
      @ModelAttribute Trip trip,
      @ModelAttribute List<Location> selectLoList,
      Model model)
      throws Exception {
    List<Location> locationList =
        tourAPIService.showLocation(trip.getCity());
    model.addAttribute("trip", trip);
    model.addAttribute("locationList", locationList);
  }

  @PostMapping("/appendMyLocation")
  @ResponseBody
  public List<LocationDTO> appendMyLocation(
      @ModelAttribute("selectLoList") List<Location> selectLoList,
      @RequestBody List<LocationDTO> locations,
      Model model) {

    selectLoList.clear();
    selectLoList.addAll(
        locations.stream()
            .map(LocationDTO::toEntity)
            .collect(Collectors.toList())
    );

    model.addAttribute("selectLoList", selectLoList);

    return selectLoList.stream()
        .map(LocationDTO::fromEntity)
        .collect(Collectors.toList());
  }

  @GetMapping("/searchLocation")
  @ResponseBody
  public List<LocationDTO> searchLocation(
      @ModelAttribute Trip trip,
      String searchText
  ){
    String text = String.format("%s %s %s",
        trip.getCity().getState().getStateName(),
        trip.getCity().getCityName().equals("전체")? "" : trip.getCity().getCityName(),
        searchText);

    List<Location> searchLoList = searchAPIService.search(text);
    return searchLoList.stream()
        .map(LocationDTO::fromEntity)
        .collect(Collectors.toList());
  }

  @RequestMapping("selectHotel")
  public void selectHotel(
      @ModelAttribute Trip trip,
      @ModelAttribute("selectHoList") List<Location> selectHoList,
      Model model)
      throws Exception {
    List<Location> hotelList =
        tourAPIService.showHotel(trip.getCity());
    model.addAttribute("trip", trip);
    model.addAttribute("hotelList", hotelList);
  }

  @PostMapping("/appendMyHotel")
  @ResponseBody
  public List<LocationDTO> appendMyHotel(
      @ModelAttribute("selectHoList") List<Location> selectHoList,
      @RequestBody List<LocationDTO> locations,
      Model model) {

    selectHoList.clear();
    if (locations != null) {
      // null 체크하면서 리스트에 추가
      locations.forEach(dto -> {
        selectHoList.add(dto != null ? dto.toEntity() : null);
      });
    }
    model.addAttribute("selectHoList", selectHoList);

    // null을 포함한 채로 DTO 변환
    return selectHoList.stream()
        .map(location -> location != null ? LocationDTO.fromEntity(location) : null)
        .collect(Collectors.toList());
  }

  @GetMapping("/searchHotel")
  @ResponseBody
  public List<LocationDTO> searchHotel(
      @ModelAttribute Trip trip,
      String searchText
  ){
    String text = String.format("%s %s %s %s",
        trip.getCity().getState().getStateName(),
        trip.getCity().getCityName().equals("전체")? "" : trip.getCity().getCityName(),
        "숙박",
        searchText);

    List<Location> searchLoList = searchAPIService.search(text);
    return searchLoList.stream()
        .map(LocationDTO::fromEntity)
        .collect(Collectors.toList());
  }


  @GetMapping("createSchedule")
  public void createSchedule(
      @ModelAttribute("selectHoList") List<Location> selectHoList,
      @ModelAttribute("selectLoList") List<Location> selectLoList,
      @ModelAttribute Trip trip,
      Model model) throws Exception {

    List<Thema> themas = scheduleService.themaList();
    model.addAttribute("themas", themas);
    model.addAttribute("Trip", trip);


    List<Schedule> tripScheduleList = new ArrayList<>();
    Map<Integer, Integer> scheduleKeyMap = new HashMap<>();
    int totalIndex = 0;
    for (int locationIndex = 0; locationIndex <= selectHoList.size(); locationIndex++) {
      Schedule schedule = new Schedule();
      int dayIndex;
      if(locationIndex == selectHoList.size()) {
        dayIndex = locationIndex-1;
      } else {
        dayIndex = locationIndex;
      }
      Location location = selectHoList.get(dayIndex);
      schedule.setScheduleNo(location.getLocationNo());
      location.setCityCode(trip.getCity().getCityCode());
      schedule.setLocation(location);
      schedule.setScheduleDay(locationIndex + 1);
      schedule.setScheduleRoute(0);
      scheduleKeyMap.put(totalIndex++, schedule.getScheduleNo());
      tripScheduleList.add(schedule);
    }

    for (int locationIndex = 0; locationIndex < selectLoList.size(); locationIndex++) {
      Schedule schedule = new Schedule();
      Location location = selectLoList.get(locationIndex);
      schedule.setScheduleNo(location.getLocationNo());
      schedule.setLocation(location);
      location.setCityCode(trip.getCity().getCityCode());
      schedule.setScheduleDay(1);
      schedule.setScheduleRoute(locationIndex + 1);
      scheduleKeyMap.put(totalIndex++, schedule.getScheduleNo());
      tripScheduleList.add(schedule);
    }

    int totalSize = selectHoList.size() + selectLoList.size() + 1;
    RouteInfo[][] distances = new RouteInfo[totalSize][totalSize];

    for (int start = 0; start < totalSize; start++) {
      for (int goal = start; goal < totalSize; goal++) {
        if (start != goal) {
          RouteInfo direction = directionService.getDirection(tripScheduleList.get(start),tripScheduleList.get(goal));
          System.out.println(direction.toString());
          distances[start][goal] = direction;
          distances[goal][start] = direction;
        }
      }
    }

    int[][] optimalRoutes = routeService.assignTourism(distances, selectHoList.size()+1, selectLoList.size());
    for (int hotel = 0; hotel < selectHoList.size()+1; hotel++) {
      for (int tour = 0; tour < optimalRoutes[hotel].length; tour++) {
        Schedule schedule = tripScheduleList.get(optimalRoutes[hotel][tour]);
        schedule.setScheduleDay(hotel + 1);
        // route는 나중에 한 번에 재설정
      }
    }

    // 마지막에 route 번호 순차적으로 재설정
    Map<Integer, List<Schedule>> daySchedules = tripScheduleList.stream()
        .collect(Collectors.groupingBy(Schedule::getScheduleDay));

    for (List<Schedule> schedules : daySchedules.values()) {
      int route = 0;
      for (Schedule schedule : schedules) {
        schedule.setScheduleRoute(route++);
      }
    }
//
//    int[][] optimalRoutes = routeService.assignTourism(distances, selectHoList.size()+1, selectLoList.size());
//    for (int hotel = 0; hotel < selectHoList.size(); hotel++) {
//      int route = 1;
//      for (int tour = 0 ; tour < optimalRoutes[hotel].length; tour++) {
//        Schedule schedule = tripScheduleList.get(optimalRoutes[hotel][tour]);
//        schedule.setScheduleDay(hotel + 1);
//        schedule.setScheduleRoute(route++);
//      }
//    }
    tripScheduleList = scheduleService.orderSchedule(tripScheduleList);
    model.addAttribute("scheduleKeyMap", scheduleKeyMap);
    model.addAttribute("distances", distances);
    model.addAttribute("tripScheduleList", tripScheduleList);

    Map<Integer, List<Schedule>> groupedSchedules = tripScheduleList.stream()
        .collect(Collectors.groupingBy(Schedule::getScheduleDay));


    System.out.println("=====================================================");
    groupedSchedules.forEach((day, schedules) -> {
      System.out.println("Day " + day + ":");
      schedules.forEach(schedule ->
          System.out.println("  Schedule No: " + schedule.getScheduleNo()
              + ", Route: " + schedule.getScheduleRoute()
              + ", Location: " + schedule.getLocation().getLocationName())
      );
    });


    model.addAttribute("groupedSchedules", groupedSchedules);
  }

  @PostMapping("update")
  @ResponseBody
  public String update(
      @RequestBody JsonNode schedules,
      @ModelAttribute("tripScheduleList") List<Schedule> tripScheduleList,
      @ModelAttribute Trip trip,
      Model model) {

    try {
      for (JsonNode scheduleNode : schedules) {
        Schedule searchSchedule = new Schedule();
        searchSchedule.setScheduleNo(scheduleNode.get("scheduleNo").asInt());
        Schedule schedule = tripScheduleList.get(tripScheduleList.indexOf(searchSchedule));
        schedule.setScheduleDay(scheduleNode.get("scheduleDay").asInt());
        schedule.setScheduleRoute(scheduleNode.get("scheduleRoute").asInt());
      }

      tripScheduleList = scheduleService.orderSchedule(tripScheduleList);
      trip.setScheduleList(tripScheduleList);
      model.addAttribute("Trip", trip);
      return "success";
    } catch (Exception e) {
      return "error: " + e.getMessage();
    }
  }

  @PostMapping("save")
  public void save(
      @ModelAttribute Trip trip,
      int themaNo,
      HttpSession session,
      SessionStatus sessionStatus) throws Exception {

    User loginUser = (User) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new Exception("로그인이 필요합니다.");
    }

    trip.setUserNo(loginUser.getUserNo());
    Thema thema = scheduleService.getThema(themaNo);
    trip.setThema(thema);
    scheduleService.saveTrip(trip);
    sessionStatus.setComplete();
  }

  @PostMapping("/invalidate")
  @ResponseBody
  public String invalidateSession(SessionStatus status) {
    status.setComplete();  // @SessionAttributes로 관리하는 세션 초기화
    return "Session invalidated";
  }
}
