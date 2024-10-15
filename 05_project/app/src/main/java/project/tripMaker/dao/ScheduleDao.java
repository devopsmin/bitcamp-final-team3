package project.tripMaker.dao;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleDao {
  List<String> cityList() throws Exception;
}
