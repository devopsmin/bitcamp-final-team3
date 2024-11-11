package project.tripMaker.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteInfo {
  private Coordinates start;
  private Coordinates goal;
  private int distance;    // 미터 단위
  private int duration;    // 밀리초 단위
  private List<Coordinates> path;

  // 내부 클래스 Coordinates
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Coordinates {
    private double longitude;
    private double latitude;
  }
}
