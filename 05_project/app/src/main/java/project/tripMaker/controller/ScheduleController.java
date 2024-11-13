package project.tripMaker.controller;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import project.tripMaker.service.*;
import project.tripMaker.vo.*;

import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Controller
@RequestMapping("/schedule")
@SessionAttributes(
    {"locationNos", "trip", "myLocations", "myHotels", "tripType", "locationList", "selectLoList",
        "hotelList", "selectHoList", "tripScheduleList"})
public class ScheduleController {

  private final CityService cityService;
  private final ScheduleService scheduleService;
  private final TourAPIService tourAPIService;
  private final DirectionService directionService;
  private final RouteService routeService;

  @RequestMapping("selectState")
  public void selectState(Model model, Trip trip) throws Exception {
    List<State> stateList = cityService.stateList();
    model.addAttribute("stateList", stateList);
    model.addAttribute("trip", trip);

    List<Location> selectLoList = new ArrayList<>();
    model.addAttribute("selectLoList", selectLoList);

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

    System.out.println("=======================sadfadsfadsfasd========"+selectLoList);

    model.addAttribute("trip", trip);
    model.addAttribute("locationList", locationList);
  }

  @PostMapping("/appendMyLocation")
  @ResponseBody
  public List<Location> appendMyLocation(
      @ModelAttribute("locationList") List<Location> locationList,
      @ModelAttribute("selectLoList") List<Location> selectLoList,
      @RequestBody List<Integer> dataIndexes,
      Model model) {

    selectLoList.clear();
    for (int index : dataIndexes) {
      selectLoList.add(locationList.get(index));
    }
    System.out.println("================================"+selectLoList);
    model.addAttribute("selectLoList", selectLoList);
    return selectLoList;
  }

  @RequestMapping("selectHotel")
  public void selectHotel(
      @ModelAttribute Trip trip,
      @ModelAttribute("selectLoList") List<Location> selectLoList,
      Model model)
      throws Exception {

    model.addAttribute("trip", trip);
    List<Location> hotelList =
        tourAPIService.showHotel(trip.getCity());
    model.addAttribute("hotelList", hotelList);
  }

  @PostMapping("/appendMyHotel")
  @ResponseBody
  public List<Location> appendMyHotel(
      @ModelAttribute("hotelList") List<Location> hotelList,
      @ModelAttribute("selectHoList") List<Location> selectHoList,
      @RequestBody List<Object> dataIndexes,  // String이나 Integer 대신 Object로 받기
      Model model) {

    for (int i = 0; i < dataIndexes.size(); i++) {
      Object index = dataIndexes.get(i);
      if (index != null) {
        // String이든 Integer든 상관없이 처리
        int idx = Integer.parseInt(index.toString());
        Location location = hotelList.get(idx);
        selectHoList.set(i, location);
      } else {
        selectHoList.set(i, null);
      }
    }

    System.out.println("Returning selectHoList: " + selectHoList);
    return selectHoList;
  }

  @GetMapping("createSchedule")
  public void createSchedule(
      @ModelAttribute("selectHoList") List<Location> selectHoList,
      @ModelAttribute("selectLoList") List<Location> selectLoList,
      @ModelAttribute Trip trip,
      Model model) throws Exception {

    List<Thema> themas = scheduleService.themaList();
    model.addAttribute("themas", themas);
    model.addAttribute("stateName", trip.getCity().getState().getStateName());
    model.addAttribute("cityName", trip.getCity().getCityName());
    model.addAttribute("startDate", trip.getStartDate());
    model.addAttribute("endDate", trip.getEndDate());
    model.addAttribute("totalDay", trip.getTotalDay());


    int index = 0;

    List<Schedule> tripScheduleList = new ArrayList<>();
    for (int i = 0; i < selectHoList.size(); i++) {
      Schedule schedule = new Schedule();
      Location location = selectHoList.get(i);
      schedule.setScheduleNo(index);
      location.setLocationNo(index++);
      location.setCityCode(trip.getCity().getCityCode());
      schedule.setLocation(location);

      schedule.setScheduleDay(i + 1);
      schedule.setScheduleRoute(0);
      tripScheduleList.add(schedule);
    }

    Schedule LastHotel = new Schedule();
    Location Lastlocation = selectHoList.get(selectHoList.size()-1);
    LastHotel.setScheduleNo(index);
    Lastlocation.setLocationNo(index++);
    Lastlocation.setCityCode(trip.getCity().getCityCode());
    LastHotel.setLocation(Lastlocation);

    LastHotel.setScheduleDay(selectHoList.size()+1);
    LastHotel.setScheduleRoute(0);
    tripScheduleList.add(LastHotel);

    for (int i = 0; i < selectLoList.size(); i++) {
      Schedule schedule = new Schedule();
      Location location = selectLoList.get(i);
      schedule.setScheduleNo(index);
      location.setLocationNo(index++);
      schedule.setLocation(location);
      location.setCityCode(trip.getCity().getCityCode());
      schedule.setScheduleDay(1);
      schedule.setScheduleRoute(i + 1);
      tripScheduleList.add(schedule);
    }

    int totalSize = selectHoList.size() + selectLoList.size() + 1;
    RouteInfo[][] distances = new RouteInfo[totalSize][totalSize];

    for (int start = 0; start < totalSize; start++) {
      for (int goal = start; goal < totalSize; goal++) {
        if (start != goal) {
//          System.out.println("=========array===================================="+tripScheduleList.get(start)+tripScheduleList.get(goal));
          RouteInfo direction = directionService.getDirection(tripScheduleList.get(start),tripScheduleList.get(goal));
          System.out.println(direction.toString());
          distances[start][goal] = direction;
          distances[goal][start] = direction;
        }
      }
    }

    for(int i = 0; i < distances.length; i++) {
      for(int j = 0; j < distances[i].length; j++) {
        if (distances[i][j] == null){
          System.out.print("000000 ");
          continue;
        }
        System.out.print(distances[i][j].getDuration()+"    ");
      }
      System.out.println(" ");
    }

    int[][] optimalRoutes = routeService.assignTourism(distances, selectHoList.size()+1, selectLoList.size());
    for (int hotel = 0; hotel < selectHoList.size(); hotel++) {
      int route = 1;
      System.out.println("숙소 " + hotel + "의 관광지: "+ tripScheduleList.get(hotel).getLocation().getLocationName() + Arrays.toString(optimalRoutes[hotel]));
      for (int tour = 0 ; tour < optimalRoutes[hotel].length; tour++) {
        Schedule schedule = tripScheduleList.get(optimalRoutes[hotel][tour]);
        schedule.setScheduleDay(hotel + 1);
        schedule.setScheduleRoute(route++);
      }
    }
    model.addAttribute("distances", distances);

    tripScheduleList = scheduleService.orderSchedule(tripScheduleList);

    Map<Integer, List<Schedule>> groupedSchedules = tripScheduleList.stream()
        .collect(Collectors.groupingBy(Schedule::getScheduleDay));
    model.addAttribute("groupedSchedules", groupedSchedules);


    model.addAttribute("tripScheduleList", tripScheduleList);
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
        System.out.println("============" + scheduleNode);
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
           // int themaNo,
      HttpSession session,
      SessionStatus sessionStatus) throws Exception {

    User loginUser = (User) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new Exception("로그인이 필요합니다.");
    }

    trip.setUserNo(loginUser.getUserNo());
       // Thema thema = scheduleService.getThema(themaNo);
       // trip.setThema(thema);
    System.out.println("==========================================");
    System.out.println(trip.toString());
    System.out.println("==========================================");
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
