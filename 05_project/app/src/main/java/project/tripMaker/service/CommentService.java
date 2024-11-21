package project.tripMaker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.tripMaker.dao.CommentDao;
import project.tripMaker.vo.Comment;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentDao commentDao;

  @Transactional
  public void add(Comment comment) throws Exception {
    commentDao.insert(comment);
  }

  public List<Comment> list(int boardNo) throws Exception {
    return commentDao.list(boardNo);
  }

  public Comment get(int commentNo) throws Exception {
    return commentDao.findBy(commentNo);
  }

  @Transactional
  public boolean update(Comment comment) throws Exception {
    if (!commentDao.update(comment)) {
      return false;
    }
    return true;
  }

  @Transactional
  public void delete(int commentNo) throws Exception {
    commentDao.delete(commentNo);
  }




  public List<Comment> listByPage(int boardNo, int page, int pageSize) throws Exception {
    int offset = (page - 1) * pageSize;
    return commentDao.listByPage(boardNo, offset, pageSize);
  }

  public List<Comment> listByLikes(int boardNo, int page, int pageSize) throws Exception {
    int offset = (page - 1) * pageSize;
    return commentDao.findCommentsByLikes(boardNo, offset, pageSize);
  }

  public int getCommentLikeCount(int commentNo) throws Exception {
    Integer commentLikeCount = commentDao.getCommentLikeCount(commentNo);
    return (commentLikeCount != null) ? commentLikeCount : 0;
  }

  // 좋아요 여부 확인
  public boolean isCommentLiked(Map<String, Object> params) throws Exception {
    return commentDao.isCommentLiked(params);
  }

  // 좋아요 추가
  @Transactional
  public void addCommentLike(Map<String, Object> params) throws Exception {
    commentDao.addCommentLike(params);
  }

  // 좋아요 삭제
  @Transactional
  public void removeCommentLike(Map<String, Object> params) throws Exception {
    commentDao.removeCommentLike(params);
  }

  @Transactional
  public Map<String, Object> toggleCommentLike( int commentNo, int userNo) throws Exception {
    Map<String, Object> params = new HashMap<>();
    params.put("commentNo", commentNo);
    params.put("userNo", userNo);

    Map<String, Object> response = new HashMap<>();
    if (isCommentLiked(params)) {
      removeCommentLike(params);
      response.put("isCommentLiked", false);
      response.put("message", "좋아요가 취소되었습니다.");
    } else {
      addCommentLike(params);
      response.put("isCommentLiked", true);
      response.put("message", "좋아요가 추가되었습니다.");
    }
    response.put("commentLikeCount", getCommentLikeCount(commentNo));
    return response;
  }

  public List<Comment> listUser(Map<String, Object> options) throws Exception {
    return commentDao.listUser(options);
  }

  public Integer countAllUserComment(Map<String, Object> options) throws Exception {
    Integer count = commentDao.countAllUserComment(options);
    return (count == null) ? 0 : count;
  }
}
