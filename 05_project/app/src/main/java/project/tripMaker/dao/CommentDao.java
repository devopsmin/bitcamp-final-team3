package project.tripMaker.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.tripMaker.vo.Comment;

@Mapper
public interface CommentDao {
  boolean insert(Comment comment) throws Exception;

  List<Comment> list(int boardNo) throws Exception;

  int countByBoardNo(int boardNo) throws Exception;

  Comment findBy(int commentNo) throws Exception;

  boolean update(Comment comment) throws Exception;

  boolean delete(@Param("commentNo")int commentNo) throws Exception;

}
