package project.tripMaker.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import project.tripMaker.vo.Board;

import java.util.List;

@Repository
public interface BoardDao {

  List<Board> mainList() throws Exception;

  Board findBy(int no) throws Exception;

  boolean insert(Board board) throws Exception;

  void updateViewCount(@Param("no") int boardNo, @Param("count") int count) throws Exception;
}
