package project.tripMaker.service;

import java.util.List;
import lombok.Data;
import org.springframework.stereotype.Service;
import project.tripMaker.dao.CityDao;
import project.tripMaker.vo.City;
import project.tripMaker.vo.State;

@Data
@Service
public class CityService {

  private final CityDao cityDao;

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
