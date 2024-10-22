package project.tripMaker.service;

import java.util.List;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import project.tripMaker.dao.ScheduleDao;
import project.tripMaker.vo.Location;
import project.tripMaker.vo.Schedule;
import project.tripMaker.vo.Thema;
import project.tripMaker.vo.Trip;

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
  public void makeTrip(Trip trip) throws Exception{
    scheduleDao.makeTrip(trip);
  }

  @Transactional
  public void saveTrip(Trip trip) {
    try {
      scheduleDao.saveTrip(trip);
    } catch (Exception e) {
      deleteTrip(trip.getTripNo());
      throw e;
    }
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void deleteTrip(int tripNo) {
    scheduleDao.deleteSchedule(tripNo);
    scheduleDao.deleteTrip(tripNo);
  }

  @Transactional
  public void addLocation(Location myLocation) {
    scheduleDao.addLocation(myLocation);
  }

  public List<Trip> getTripList(Trip trip) {
    return scheduleDao.getTripList(trip);
  }

  public Thema getThema(int themaNo) {
    return scheduleDao.getThema(themaNo);
  }
}
