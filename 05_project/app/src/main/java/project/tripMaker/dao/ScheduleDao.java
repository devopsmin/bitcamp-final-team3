package project.tripMaker.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import project.tripMaker.vo.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface ScheduleDao {
  void makeTrip(Trip trip);

  List<Location> locationList(String cityCode);

  List<Location> hotelList(String cityCode);

  void addSchedule(Schedule schedule);

  Location findLocation(int locationNo);

  List<Schedule> viewSchedule(Integer tripNo);

  List<Thema> themaList();

  void saveTrip(Trip trip);

  void addLocation(Location myLocation);

  void deleteTrip(int tripNo);

  void deleteSchedule(int tripNo);

  List<Trip> getTripList(Trip trip);

  Thema getThema(int themaNo);

  List<Trip> findTripsByUserNo(Long userNo);

  List<Integer> findTripNosByUserNo(Long userNo);

  List<Schedule> findSchedulesByTripNo(int tripNo);

  List<Trip> findSchedulesByTripNoExcludeBoard(Long userNo);
}
