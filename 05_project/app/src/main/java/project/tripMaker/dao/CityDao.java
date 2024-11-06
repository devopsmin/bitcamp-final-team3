package project.tripMaker.dao;

import org.apache.ibatis.annotations.Mapper;
import project.tripMaker.vo.City;
import project.tripMaker.vo.State;

import java.util.List;


@Mapper
public interface CityDao {

  List<State> stateList();

  List<City> cityList(String stateCode);

  City findCity(String cityCode);
}
