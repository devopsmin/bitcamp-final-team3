package project.tripMaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.tripMaker.dao.ScheduleDao;

import java.util.List;

@Service
public class ScheduleService {

  @Autowired
  ScheduleDao scheduleDao;

  public List<String> cityList() throws Exception {
    return scheduleDao.cityList();
  }
}
