package project.tripMaker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.tripMaker.dao.CommentDao;
import project.tripMaker.vo.Board;
import project.tripMaker.vo.Comment;

import java.util.List;

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

}
