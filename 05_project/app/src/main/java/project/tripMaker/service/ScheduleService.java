package project.tripMaker.service;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import project.tripMaker.dao.ScheduleDao;
import project.tripMaker.vo.Location;
import project.tripMaker.vo.Schedule;
import project.tripMaker.vo.Thema;
import project.tripMaker.vo.Trip;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

@Data
@Service
public class ScheduleService {

  private final ScheduleDao scheduleDao;

  public List<Location> locationList(String cityCode) throws Exception {
    return scheduleDao.locationList(cityCode);
  }

  public List<Location> hotelList(String cityCode) throws Exception {
    return scheduleDao.hotelList(cityCode);
  }

  @Transactional
  public void addSchedule(Schedule schedule) throws Exception {
    scheduleDao.addSchedule(schedule);
  }

  public Location findLocation(int locationNo) throws Exception {
    return scheduleDao.findLocation(locationNo);
  }

  public List<Schedule> viewSchedule(Integer tripNo) {
    return scheduleDao.viewSchedule(tripNo);
  }

  public List<Thema> themaList() {
    return scheduleDao.themaList();
  }

  @Transactional
  public void makeTrip(Trip trip) throws Exception {
    scheduleDao.makeTrip(trip);
  }

  @Transactional
  public void saveTrip(Trip trip) {
    scheduleDao.makeTrip(trip);
    for (Schedule schedule : trip.getScheduleList()) {
      schedule.setTripNo(trip.getTripNo());
      scheduleDao.addLocation(schedule.getLocation());
      scheduleDao.addSchedule(schedule);
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteTrip(int tripNo) {
    scheduleDao.deleteSchedule(tripNo);
    scheduleDao.deleteTrip(tripNo);
  }
  //
  //  @Transactional
  //  public void addLocation(Location myLocation) {
  //    scheduleDao.addLocation(myLocation);
  //  }

  public List<Trip> getTripList(Trip trip) {
    return scheduleDao.getTripList(trip);
  }

  public Thema getThema(int themaNo) {
    return scheduleDao.getThema(themaNo);
  }

  public List<Schedule> orderSchedule(List<Schedule> scheduleList) {
    if (scheduleList == null || scheduleList.size() == 0) {
      return null;
    }
    scheduleList.sort(Comparator
            .comparing(Schedule::getScheduleDay)
            .thenComparing(Schedule::getScheduleRoute)
    );
    return scheduleList;
  }

  public void calculateDay(Trip trip) {
    LocalDate start = trip.getStartDate().toLocalDate();
    LocalDate end = trip.getEndDate().toLocalDate();
    trip.setTotalDay((int) ChronoUnit.DAYS.between(start, end));
  }


  public List<Trip> getTripsByUserNo(Long userNo) {
    return scheduleDao.findTripsByUserNo(userNo);
  }

  // user_no로 trip_no 리스트 가져오기
  public List<Integer> getTripNosByUserNo(Long userNo) {
    return scheduleDao.findTripNosByUserNo(userNo);
  }

  // 특정 trip_no로 schedule 리스트 가져오기
  public List<Schedule> getSchedulesByTripNo(int tripNo) {
    return scheduleDao.findSchedulesByTripNo(tripNo);
  }

  // 게시글이 있는경우 제외하고 schedule 리스트 가져오기
  public List<Trip> getSchedulesByTripNoExcludeBoard(Long userNo) {
    return scheduleDao.findSchedulesByTripNoExcludeBoard(userNo);
  }
}

















