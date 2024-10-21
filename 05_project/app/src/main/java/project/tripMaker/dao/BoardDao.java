package project.tripMaker.dao;

import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import project.tripMaker.vo.Board;

import java.util.List;
import project.tripMaker.vo.Comment;

@Mapper
public interface BoardDao {
  List<Board> list(Map<String, Object> options) throws Exception;
  List<Board> listLike(Map<String, Object> options) throws Exception;
  List<Board> listFavor(Map<String, Object> options) throws Exception;
  List<Board> listView(Map<String, Object> options) throws Exception;

  boolean insert(Board board) throws Exception;

  Board findBy(int boardNo) throws Exception;

  void updateViewCount(@Param("boardNo")int boardNo, @Param("boardCount")int boardCount) throws Exception;

  int countAll() throws Exception;

  boolean delete(@Param("boardNo")int boardNo) throws Exception;

  boolean update(Board board) throws Exception;
}
