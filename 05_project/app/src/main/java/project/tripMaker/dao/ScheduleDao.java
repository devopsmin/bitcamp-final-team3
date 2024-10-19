package project.tripMaker.dao;

import org.springframework.stereotype.Repository;
import project.tripMaker.vo.*;

import java.util.List;
import java.util.Map;

@Repository
public interface ScheduleDao {
  void makeTrip(Trip trip);

  void updateTrip(Trip trip);

  List<Location> locationList(String cityCode);

  List<Location> hotelList(String cityCode);

  void addSchedule(Schedule schedule);

  Location findLocation(int locationNo);

  List<Schedule> viewSchedule(Integer tripNo);

  Map<Integer, String> selectThema();
}
