package project.tripMaker.service;

import lombok.Data;
import org.springframework.stereotype.Service;
import project.tripMaker.vo.RouteInfo;
import project.tripMaker.vo.Schedule;

import java.util.*;
import java.util.stream.Collectors;

@Data
@Service

public class RouteService {
  public int[][] assignTourism(RouteInfo[][] routeInfos, int accommodationCount, int tourismCount) {
    int[][] result = new int[accommodationCount][];
    int totalLocations = accommodationCount + tourismCount;

    int maxLocationsPerDay = (int) Math.ceil((double) tourismCount / accommodationCount);

    System.out.println("====================================================");
    System.out.println("==================================숙소 갯수 : " + accommodationCount);
    System.out.println("일별 최대 관광지 수: " + maxLocationsPerDay);

    List<List<Integer>> assignments = new ArrayList<>(accommodationCount);
    for (int i = 0; i < accommodationCount; i++) {
      assignments.add(new ArrayList<>());
    }

    List<Integer> unassignedTourism = new ArrayList<>();
    for (int i = accommodationCount; i < totalLocations; i++) {
      unassignedTourism.add(i);
    }

    for (int day = 0; day < accommodationCount; day++) {
      int currentAccommodation = (day < accommodationCount - 1) ? day : accommodationCount - 2;
      int nextAccommodation = (day < accommodationCount - 1) ? day + 1 : day;

      int remainingDays = accommodationCount - day;
      int remainingTourism = unassignedTourism.size();
      int currentDayMax = Math.min(maxLocationsPerDay,
          (int) Math.ceil((double) remainingTourism / remainingDays));

      if (day == accommodationCount - 1) {
        currentDayMax = remainingTourism; // 마지막 날은 모든 관광지를 배정
      }

      while (!unassignedTourism.isEmpty() && assignments.get(day).size() < currentDayMax) {
        int bestTourism = -1;
        long bestScore = Long.MAX_VALUE;

        for (int tourism : unassignedTourism) {
          long totalTime = routeInfos[currentAccommodation][tourism].getDuration() +
              routeInfos[tourism][nextAccommodation].getDuration();

          if (totalTime < bestScore) {
            bestScore = totalTime;
            bestTourism = tourism;
          }
        }

        if (bestTourism != -1) {
          assignments.get(day).add(bestTourism);
          unassignedTourism.remove(Integer.valueOf(bestTourism));
        } else {
          break;
        }
      }

      List<Integer> dayRoute = optimizeRouteForDay(
          routeInfos,
          currentAccommodation,
          nextAccommodation,
          assignments.get(day)
      );

      result[day] = dayRoute.stream().mapToInt(Integer::intValue).toArray();

      // 누락된 관광지 확인 및 디버깅
      System.out.println("Day " + (day + 1) + ": Assigned " + dayRoute);
      System.out.println("Remaining unassigned tourism: " + unassignedTourism);
    }

    // 마지막으로 남은 관광지가 있다면 배정
    if (!unassignedTourism.isEmpty()) {
      System.out.println("Unassigned tourism spots after all days: " + unassignedTourism);

      // 남은 관광지를 마지막 날에 배정
      result[accommodationCount - 1] = unassignedTourism.stream()
          .mapToInt(Integer::intValue) // Integer -> int 변환
          .toArray();
    }

    return result;
  }

  private void updateScheduleRoutes(List<Schedule> tripScheduleList) {
    Map<Integer, List<Schedule>> daySchedules = tripScheduleList.stream()
        .collect(Collectors.groupingBy(Schedule::getScheduleDay));

    for (List<Schedule> daySchedule : daySchedules.values()) {
      int route = 0;
      for (Schedule schedule : daySchedule) {
        schedule.setScheduleRoute(route++);
      }
    }
  }

  private List<Integer> optimizeRouteForDay(RouteInfo[][] routeInfos, int startAccommodation,
      int endAccommodation, List<Integer> tourismSpots) {
    List<Integer> optimizedRoute = new ArrayList<>();
    if (tourismSpots.isEmpty()) {
      return optimizedRoute;
    }

    int currentLocation = startAccommodation;
    Set<Integer> unvisited = new HashSet<>(tourismSpots);

    while (!unvisited.isEmpty()) {
      int nextLocation = -1;
      long bestScore = Long.MAX_VALUE;

      for (int tourism : unvisited) {
        long currentToTourism = routeInfos[currentLocation][tourism].getDuration();
        long tourismToNext = unvisited.size() == 1 ?
            routeInfos[tourism][endAccommodation].getDuration() : 0;
        long totalTime = currentToTourism + tourismToNext;

        if (totalTime < bestScore) {
          bestScore = totalTime;
          nextLocation = tourism;
        }
      }

      if (nextLocation != -1) {
        optimizedRoute.add(nextLocation);
        unvisited.remove(nextLocation);
        currentLocation = nextLocation;
      }
    }

    return optimizedRoute;
  }
}
