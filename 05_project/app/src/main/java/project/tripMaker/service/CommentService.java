package project.tripMaker.service;

import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.tripMaker.dao.BoardDao;
import project.tripMaker.dao.CommentDao;
import project.tripMaker.vo.Board;
import project.tripMaker.vo.Comment;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentDao commentDao;

  public List<Comment> list(int boardNo) throws Exception {
    return commentDao.list(boardNo);
  }

  @Transactional
  public void add(Comment comment) throws Exception {
    commentDao.insert(comment);
  }

  @Transactional
  public void delete(int commentNo) throws Exception {
    commentDao.delete(commentNo);
  }

  @Transactional
  public boolean update(Comment comment) throws Exception {
    if (!commentDao.update(comment)) {
      return false;
    }
    return true;
  }

  public Comment get(int commentNo) throws Exception {
    return commentDao.findBy(commentNo);
  }

}
