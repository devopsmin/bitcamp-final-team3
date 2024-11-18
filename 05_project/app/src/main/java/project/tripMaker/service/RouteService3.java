package project.tripMaker.service;

import lombok.Data;
import org.springframework.stereotype.Service;
import project.tripMaker.vo.RouteInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Service
public class RouteService3 {
  static class Edge implements Comparable<Edge> {
    int src, dest;
    long duration;

    Edge(int src, int dest, long duration) {
      this.src = src;
      this.dest = dest;
      this.duration = duration;
    }

    @Override
    public int compareTo(Edge other) {
      return Long.compare(this.duration, other.duration);
    }
  }

  static class UnionFind {
    int[] parent;

    UnionFind(int n) {
      parent = new int[n];
      for (int i = 0; i < n; i++) {
        parent[i] = i;
      }
    }

    int find(int x) {
      if (parent[x] != x) {
        parent[x] = find(parent[x]);
      }
      return parent[x];
    }

    void union(int x, int y) {
      int px = find(x);
      int py = find(y);
      if (px != py) {
        parent[px] = py;
      }
    }
  }

  public int[][] assignTourism(RouteInfo[][] routeInfos, int accommodationCount, int tourismCount) {
    int[][] result = new int[accommodationCount][];  // assignments.length 대신 accommodationCount 사용
    List<List<Integer>> assignments = new ArrayList<>(accommodationCount);  // 크기 명시

    System.out.println("====================================================");
    System.out.println("==================================숙소 갯수 : "+ accommodationCount);

    for (int i = 0; i < accommodationCount; i++) {
      assignments.add(new ArrayList<>());
    }

    // 각 관광지의 배정 여부를 추적
    boolean[] assignedTourism = new boolean[tourismCount];

    // 1. 각 숙소에 가장 가까운 관광지 하나씩 우선 배정
    for (int i = 0; i < accommodationCount; i++) {
      int nearestTourism = findNearestUnassignedTourism(routeInfos, i, accommodationCount, assignedTourism);
      if (nearestTourism != -1) {
        assignments.get(i).add(nearestTourism);
        assignedTourism[nearestTourism - accommodationCount] = true;
      }
    }

    // 2. 남은 관광지들을 가장 가까운 숙소에 배정
    for (int j = 0; j < tourismCount; j++) {
      if (!assignedTourism[j]) {
        int tourismIndex = j + accommodationCount;
        int bestAccommodation = findClosestAccommodation(routeInfos, tourismIndex, accommodationCount);
        if (bestAccommodation != -1) {
          assignments.get(bestAccommodation).add(tourismIndex);
          assignedTourism[j] = true;
        }
      }
    }

    // List를 배열로 변환할 때도 accommodationCount 사용
    for (int i = 0; i < accommodationCount; i++) {
      List<Integer> tourismList = assignments.get(i);
      result[i] = tourismList.stream().mapToInt(Integer::intValue).toArray();
    }

    return result;
  }

  public int[][] findOptimalRoutes(RouteInfo[][] routeInfos, int accommodationCount, int tourismCount) {
    int[][] assignments = assignTourism(routeInfos, accommodationCount, tourismCount);
    int[][] routes = new int[accommodationCount][];  // 명시적으로 accommodationCount 사용

    for (int i = 0; i < accommodationCount; i++) {
      routes[i] = findOptimalRouteForAccommodation(routeInfos, i, assignments[i]);
    }

    return routes;
  }

  private int[] findOptimalRouteForAccommodation(RouteInfo[][] routeInfos, int accommodation, int[] assignedTourism) {
    int n = assignedTourism.length + 1; // 숙소 + 배정된 관광지
    List<Integer> route = new ArrayList<>();
    route.add(accommodation);

    // 현재 위치에서 가장 가까운 다음 관광지를 찾아가는 방식
    boolean[] visited = new boolean[assignedTourism.length];
    int currentLocation = accommodation;

    while (route.size() < n) {
      int nextLocation = findNearestLocation(routeInfos, currentLocation, assignedTourism, visited);
      if (nextLocation != -1) {
        route.add(nextLocation);
        visited[Arrays.binarySearch(assignedTourism, nextLocation)] = true;
        currentLocation = nextLocation;
      }
    }

    // 다시 시작 숙소로 돌아가기
    route.add(accommodation);

    return route.stream().mapToInt(Integer::intValue).toArray();
  }

  private int findNearestUnassignedTourism(RouteInfo[][] routeInfos, int accommodation,
      int accommodationCount, boolean[] assignedTourism) {
    int nearest = -1;
    long minDuration = Long.MAX_VALUE;

    for (int i = accommodationCount; i < routeInfos.length; i++) {
      if (!assignedTourism[i - accommodationCount] && routeInfos[accommodation][i] != null) {
        long duration = routeInfos[accommodation][i].getDuration();
        if (duration < minDuration) {
          minDuration = duration;
          nearest = i;
        }
      }
    }

    return nearest;
  }

  private int findClosestAccommodation(RouteInfo[][] routeInfos, int tourism, int accommodationCount) {
    int closest = -1;
    long minDuration = Long.MAX_VALUE;

    for (int i = 0; i < accommodationCount; i++) {
      if (routeInfos[i][tourism] != null) {
        long duration = routeInfos[i][tourism].getDuration();
        if (duration < minDuration) {
          minDuration = duration;
          closest = i;
        }
      }
    }

    return closest;
  }

  private int findNearestLocation(RouteInfo[][] routeInfos, int current, int[] possibleLocations, boolean[] visited) {
    int nearest = -1;
    long minDuration = Long.MAX_VALUE;

    for (int i = 0; i < possibleLocations.length; i++) {
      if (!visited[i] && routeInfos[current][possibleLocations[i]] != null) {
        long duration = routeInfos[current][possibleLocations[i]].getDuration();
        if (duration < minDuration) {
          minDuration = duration;
          nearest = possibleLocations[i];
        }
      }
    }

    return nearest;
  }
}
