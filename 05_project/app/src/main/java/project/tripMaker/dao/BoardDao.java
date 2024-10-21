package project.tripMaker.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.tripMaker.vo.Board;

import java.util.List;

@Mapper
public interface BoardDao {

  List<Board> list();

  List<Board> listByLikes();

  List<Board> listByFavorites();

  List<Board> listByViews();

  Board findBy(@Param("boardNo") int boardNo);

  void insert(Board board);

  boolean update(Board board);

  void delete(@Param("boardNo") int boardNo);

  void updateViewCount(@Param("no") int boardNo, @Param("count") int count) throws Exception;
}