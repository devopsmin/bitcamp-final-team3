package project.tripMaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.tripMaker.dao.CityDao;

@Service
public class CItyService {

  @Autowired
  CityDao cityDao;
}
