package project.tripMaker.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.tripMaker.dao.ScheduleDao;
import project.tripMaker.vo.*;

import java.util.List;
import java.util.Map;

@Data
@Service
public class ScheduleService {

  private final ScheduleDao scheduleDao;

  public void makeTrip(Trip trip) throws Exception{
    scheduleDao.makeTrip(trip);
  }

  public void updateTrip(Trip trip) throws Exception{
    scheduleDao.updateTrip(trip);
  }

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

  public List<Thema> getThema() {
    return scheduleDao.getThema();
  }

  public void saveTrip(Trip trip) {
    scheduleDao.saveTrip(trip);
  }

  @Transactional
  public void addLocation(Location myLocation) {
    scheduleDao.addLocation(myLocation);
  }
}
