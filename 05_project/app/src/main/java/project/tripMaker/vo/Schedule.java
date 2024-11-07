package project.tripMaker.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "scheduleNo")
public class Schedule {
  private Integer scheduleNo;      // 여행일정번호
  private Location location;      // 여행지정보 번호
  private Integer tripNo;          // 여행번호
  private Integer scheduleDay;     // 여행일차
  private Integer scheduleRoute;   // 여행순서
}
