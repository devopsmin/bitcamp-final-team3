package project.tripMaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.tripMaker.dao.ScheduleDao;
import project.tripMaker.vo.*;

import java.util.List;

@Service
public class ScheduleService {

  @Autowired
  ScheduleDao scheduleDao;

  public void makeTrip(Trip trip) throws Exception{
    scheduleDao.makeTrip(trip);
  }

  public void updateTrip(Trip trip) throws Exception{
    scheduleDao.updateTrip(trip);
  }

  public List<State> stateList() throws Exception {
    return scheduleDao.stateList();
  }

  public List<City> cityList(String stateCode) throws Exception {
    return scheduleDao.cityList(stateCode);
  }

  public List<Location> locationList(String cityCode) throws Exception {
    return scheduleDao.locationList(cityCode);
  }

  public List<Location> hotelList(String cityCode) throws Exception {
    return scheduleDao.hotelList(cityCode);
  }

  public void addSchedule(Schedule schedule) throws Exception {
    scheduleDao.addSchedule(schedule);
  }

  public Location findLocation(int locationNo) throws Exception {
    return scheduleDao.findLocation(locationNo);
  }

  public City firndCity(String cityCode) {
    return scheduleDao.findCity(cityCode);
  }

  public List<Schedule> viewSchedule(Integer tripNo) {
    return scheduleDao.viewSchedule(tripNo);

  }
}
