package project.tripMaker.vo;
public class Schedule {
  private Integer scheduleNo;      // 여행일정번호
  private Location location;      // 여행지정보 번호
  private Integer tripNo;          // 여행번호
  private Integer scheduleDay;     // 여행일차
  private Integer scheduleRoute;   // 여행순서

  public Integer getScheduleNo() {
    return scheduleNo;
  }

  public void setScheduleNo(Integer scheduleNo) {
    this.scheduleNo = scheduleNo;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
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

  @Override
  public String toString() {
    return "Schedule{" +
        "scheduleNo=" + scheduleNo +
        ", location=" + location +
        ", tripNo=" + tripNo +
        ", scheduleDay=" + scheduleDay +
        ", scheduleRoute=" + scheduleRoute +
        '}';
  }
}
