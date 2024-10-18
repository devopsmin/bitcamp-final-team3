package project.tripMaker.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import project.tripMaker.vo.Board;

import java.util.List;
import project.tripMaker.vo.Comment;

@Repository
public interface BoardDao {
  List<Board> list() throws Exception;
  List<Board> findAllOrderByBoardLikeDesc();
  List<Board> findAllOrderByBoardFavorDesc();
  List<Board> findAllOrderByBoardCountDesc();

  boolean insert(Board board) throws Exception;

  Board findBy(int boardNo) throws Exception;

  void updateViewCount(@Param("boardNo")int boardNo, @Param("boardCount")int boardCount) throws Exception;

  boolean delete(@Param("boardNo")int boardNo) throws Exception;

  boolean update(Board board) throws Exception;
}
