package project.tripMaker.dao;

import org.springframework.stereotype.Repository;
import project.tripMaker.vo.City;
import project.tripMaker.vo.Location;
import project.tripMaker.vo.Schedule;
import project.tripMaker.vo.State;

import java.util.List;

@Repository
public interface ScheduleDao {
  List<State> stateList();

  List<City> cityList(String stateCode);

  List<Location> locationList(String cityCode);

  List<Location> hotelList(String cityCode);

  void addSchedule(Schedule schedule);

  Location findLocation(int locationNo);
}
