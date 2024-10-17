package project.tripMaker.dao;

import org.springframework.stereotype.Repository;
import project.tripMaker.vo.*;

import java.util.List;

@Repository
public interface ScheduleDao {
  void makeTrip(Trip trip);

  void updateTrip(Trip trip);

  List<State> stateList();

  List<City> cityList(String stateCode);

  List<Location> locationList(String cityCode);

  List<Location> hotelList(String cityCode);

  void addScheduleList(Trip trip);

  Location findLocation(int locationNo);
}
