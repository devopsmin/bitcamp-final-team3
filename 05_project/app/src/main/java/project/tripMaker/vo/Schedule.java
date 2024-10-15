package project.tripMaker.vo;

public class Schedule {
  private Integer scheduleNo;      // 여행일정번호
  private Integer locationNo;      // 여행지정보 번호
  private Integer tripNo;          // 여행번호
  private Integer scheduleDay;     // 여행일차
  private Integer scheduleRoute;   // 여행순서

  @Override
  public String toString() {
    return "Schedule{" +
        "scheduleNo=" + scheduleNo +
        ", locationNo=" + locationNo +
        ", tripNo=" + tripNo +
        ", scheduleDay=" + scheduleDay +
        ", scheduleRoute=" + scheduleRoute +
        '}';
  }

  public Integer getScheduleNo() {
    return scheduleNo;
  }

  public void setScheduleNo(Integer scheduleNo) {
    this.scheduleNo = scheduleNo;
  }

  public Integer getLocationNo() {
    return locationNo;
  }

  public void setLocationNo(Integer locationNo) {
    this.locationNo = locationNo;
  }

  public Integer getTripNo() {
    return tripNo;
  }

  public void setTripNo(Integer tripNo) {
    this.tripNo = tripNo;
  }

  public Integer getScheduleDay() {
    return scheduleDay;
  }

  public void setScheduleDay(Integer scheduleDay) {
    this.scheduleDay = scheduleDay;
  }

  public Integer getScheduleRoute() {
    return scheduleRoute;
  }

  public void setScheduleRoute(Integer scheduleRoute) {
    this.scheduleRoute = scheduleRoute;
  }
}
