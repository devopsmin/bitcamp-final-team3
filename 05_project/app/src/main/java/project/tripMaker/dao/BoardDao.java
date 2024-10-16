package project.tripMaker.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import project.tripMaker.vo.Board;

import java.util.List;

@Repository
public interface BoardDao {
  boolean insert(Board board) throws Exception;

  List<Board> list() throws Exception;

  Board findBy(int boardNo) throws Exception;

  boolean update(Board board) throws Exception;

  boolean delete(int boardNo) throws Exception;

  void updateBoardCount(@Param("boardNo") int boardNo, @Param("boardCount") int boardCount) throws Exception;

}
