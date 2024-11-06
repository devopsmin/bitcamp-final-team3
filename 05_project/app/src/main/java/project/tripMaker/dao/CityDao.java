package project.tripMaker.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import project.tripMaker.vo.City;
import project.tripMaker.vo.State;


@Mapper
public interface CityDao {

  List<State> stateList();

  List<City> cityList(String stateCode);

  City findCity(String cityCode);
}
