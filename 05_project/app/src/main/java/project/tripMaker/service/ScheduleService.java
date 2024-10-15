package project.tripMaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.tripMaker.dao.ScheduleDao;
import project.tripMaker.vo.City;
import project.tripMaker.vo.Location;
import project.tripMaker.vo.State;

import java.util.List;

@Service
public class ScheduleService {

  @Autowired
  ScheduleDao scheduleDao;

  public List<City> cityList() throws Exception {
    return scheduleDao.cityList();
  }

  public List<State> stateList(String cityCode) throws Exception {
    return scheduleDao.stateList(cityCode);
  }

  public List<Location> locationList(String stateCode) throws Exception {
    return scheduleDao.locationList(stateCode);
  }

  public List<Location> hotelList(String stateName) throws Exception {
    return scheduleDao.locationList(stateName);
  }
}
