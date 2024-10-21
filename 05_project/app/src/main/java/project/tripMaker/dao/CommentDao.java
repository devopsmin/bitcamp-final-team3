package project.tripMaker.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.tripMaker.vo.Board;
import project.tripMaker.vo.Comment;

@Mapper
public interface CommentDao {
  List<Comment> list(int boardNo) throws Exception;

  boolean insert(Comment comment) throws Exception;

  Comment findBy(int commentNo) throws Exception;

  boolean delete(@Param("commentNo")int commentNo) throws Exception;

  boolean update(Comment comment) throws Exception;
}
