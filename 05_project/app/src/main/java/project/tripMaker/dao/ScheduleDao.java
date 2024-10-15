package project.tripMaker.dao;

import org.springframework.stereotype.Repository;
import project.tripMaker.vo.City;
import project.tripMaker.vo.Location;
import project.tripMaker.vo.State;

import java.util.List;

@Repository
public interface ScheduleDao {
  List<City> cityList();

  List<State> stateList(String cityCode);

  List<Location> locationList(String stateCode);

  List<Location> hotelList(String stateName);

}
