package project.tripMaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.tripMaker.vo.Board;
import project.tripMaker.dao.BoardDao;

import java.util.List;

@Service
public class BoardService {

  @Autowired
  BoardDao boardDao;

  public List<Board> list() throws Exception {
    return boardDao.list();
  }

  public List<Board> listByLikes() {
    return boardDao.findAllOrderByBoardLikeDesc();
  }

  public List<Board> listByFavorites() {
    return boardDao.findAllOrderByBoardFavorDesc();
  }

  public List<Board> listByViews() {
    return boardDao.findAllOrderByBoardCountDesc();
  }


  @Transactional
  public void add(Board board) throws Exception {
    boardDao.insert(board);
  }

  @Transactional
  public void increaseViewCount(int boardNo) throws Exception {
    Board board = boardDao.findBy(boardNo);
    if (board != null) {
      boardDao.updateViewCount(board.getBoardNo(), board.getBoardCount());
    }
  }

  public Board get(int boardNo) throws Exception {
    return boardDao.findBy(boardNo);
  }

  @Transactional
  public void delete(int boardNo) throws Exception {
    boardDao.delete(boardNo);
  }

  @Transactional
  public boolean update(Board board) throws Exception {
    if (!boardDao.update(board)) {
      return false;
    }
    return true;
  }

}
