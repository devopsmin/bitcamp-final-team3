package project.tripMaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.tripMaker.dao.CityDao;
import project.tripMaker.vo.City;
import project.tripMaker.vo.State;

import java.util.List;

@Service
public class CityService {

  @Autowired
  CityDao cityDao;

  public List<State> stateList() throws Exception {
    return cityDao.stateList();
  }

  public List<City> cityList(String stateCode) throws Exception {
    return cityDao.cityList(stateCode);
  }

  public City firndCity(String cityCode) {
    return cityDao.findCity(cityCode);
  }
}
