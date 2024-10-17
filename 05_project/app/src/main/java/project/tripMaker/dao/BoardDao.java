package project.tripMaker.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import project.tripMaker.vo.Board;

import java.util.List;

@Repository
public interface BoardDao {
  boolean insert(Board board) throws Exception;

  List<Board> list() throws Exception;

  Board findBy(int no) throws Exception;

  void updateViewCount(@Param("no") int boardNo, @Param("count") int count) throws Exception;
}
