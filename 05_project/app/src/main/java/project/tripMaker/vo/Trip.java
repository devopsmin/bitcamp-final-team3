package project.tripMaker.vo;

import java.sql.Date;
import java.util.List;
import lombok.Data;

@Data
public class Trip {
  private Integer tripNo;          // 여행번호
  private Long userNo;          // 유저번호
  private Thema thema;
  private City city;
  private String tripTitle;        // 여행제목
  private Date startDate;          // 여행시작일
  private Date endDate;            // 여행종료일
  private Date tripCreatedDate;    // 생성날짜
  private List<Schedule> scheduleList;
  private Integer totalDay;
}
