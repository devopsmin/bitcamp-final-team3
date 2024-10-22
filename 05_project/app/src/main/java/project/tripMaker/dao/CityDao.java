package project.tripMaker.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import project.tripMaker.vo.City;
import project.tripMaker.vo.State;


@Mapper
public interface CityDao {

  List<State> stateList();

  List<City> cityList(String stateCode);

  City findCity(String cityCode);
}
