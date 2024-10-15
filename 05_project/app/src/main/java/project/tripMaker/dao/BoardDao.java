package project.tripMaker.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import project.tripMaker.vo.Board;

import java.util.List;

@Repository
public interface BoardDao {
  List<Board> list() throws Exception;

  boolean insert(Board board) throws Exception;
}
