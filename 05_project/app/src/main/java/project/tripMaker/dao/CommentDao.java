package project.tripMaker.dao;

import java.util.List;
import java.util.Map;

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




  List<Comment> listByPage(@Param("boardNo") int boardNo, @Param("offset") int offset, @Param("limit") int limit) throws Exception;

  List<Comment> findCommentsByLikes(@Param("boardNo") int boardNo, @Param("offset") int offset, @Param("limit") int limit) throws Exception;

  Integer getCommentLikeCount(int commentNo); // 좋아요 수 조회
  boolean isCommentLiked(Map<String, Object> params); // 좋아요 여부 확인
  void addCommentLike(Map<String, Object> params); // 좋아요 추가
  void removeCommentLike(Map<String, Object> params); // 좋아요 삭제


  List<Comment> listUser(Map<String, Object> options) throws Exception;
  Integer countAllUserComment(Map<String, Object> options) throws Exception;

}
