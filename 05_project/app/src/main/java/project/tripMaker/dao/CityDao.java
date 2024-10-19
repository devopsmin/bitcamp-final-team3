package project.tripMaker.dao;

import org.springframework.stereotype.Repository;
import project.tripMaker.vo.City;
import project.tripMaker.vo.State;

import java.util.List;


@Repository
public interface CityDao {

  List<State> stateList();

  List<City> cityList(String stateCode);

  City findCity(String cityCode);
}
