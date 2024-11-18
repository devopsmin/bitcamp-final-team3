package project.tripMaker.dao;

import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import project.tripMaker.vo.Ben;

@Mapper
public interface BenDao {

  void insert(Ben ben) throws Exception;

  Ben findByUserNo(Long userNo) throws Exception;

  boolean update(Ben ben) throws Exception;

  List<Ben> findExpiredBans(LocalDateTime now) throws Exception;
}
