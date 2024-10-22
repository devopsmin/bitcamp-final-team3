package project.tripMaker.vo;

import java.sql.Date;
import java.util.List;

public class Trip {
  private Integer tripNo;          // 여행번호
  private Integer userNo;          // 유저번호
  private Integer themaNo;         // 여행테마번호
  private City city;
  private String tripTitle;        // 여행제목
  private Date startDate;          // 여행시작일
  private Date endDate;            // 여행종료일
  private Date tripCreatedDate;    // 생성날짜
  private List<Schedule> scheduleList;


  @Override
  public String toString() {
    return "Trip{" +
        "tripNo=" + tripNo +
        ", userNo=" + userNo +
        ", themaNo=" + themaNo +
        ", tripTitle='" + tripTitle + '\'' +
        ", startDate=" + startDate +
        ", endDate=" + endDate +
        ", tripCreatedDate=" + tripCreatedDate +
        '}';
  }

  public Integer getTripNo() {
    return tripNo;
  }

  public void setTripNo(Integer tripNo) {
    this.tripNo = tripNo;
  }

  public Integer getUserNo() {
    return userNo;
  }

  public void setUserNo(Integer userNo) {
    this.userNo = userNo;
  }

  public Integer getThemaNo() {
    return themaNo;
  }

  public void setThemaNo(Integer themaNo) {
    this.themaNo = themaNo;
  }

  public City getCity() {
    return city;
  }

  public void setCity(City city) {
    this.city = city;
  }

  public String getTripTitle() {
    return tripTitle;
  }

  public void setTripTitle(String tripTitle) {
    this.tripTitle = tripTitle;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Date getTripCreatedDate() {
    return tripCreatedDate;
  }

  public void setTripCreatedDate(Date tripCreatedDate) {
    this.tripCreatedDate = tripCreatedDate;
  }

  public List<Schedule> getScheduleList() {
    return scheduleList;
  }

  public void setScheduleList(List<Schedule> scheduleList) {
    this.scheduleList = scheduleList;
  }
}
