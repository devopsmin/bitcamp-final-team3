package project.tripMaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.tripMaker.vo.Board;
import project.tripMaker.dao.BoardDao;
import project.tripMaker.vo.Comment;

import java.util.List;

@Service
public class BoardService {

  @Autowired
  BoardDao boardDao;


  @Transactional
  public void add(Board board) throws Exception {
    boardDao.insert(board);
  }

  public List<Board> list() throws Exception {
    return boardDao.list();
  }

  public Board get(int boardNo) throws Exception {
    return boardDao.findBy(boardNo);
  }

  @Transactional
  public void increaseBoardCount(int boardNo) throws Exception {
    Board board = boardDao.findBy(boardNo);
    if (board != null) {
      boardDao.updateBoardCount(board.getBoardNo(), board.getBoardCount() + 1);
    }
  }

  @Transactional
  public boolean update(Board board) throws Exception {
    if (!boardDao.update(board)) {
      return false;
    }
    return true;
  }

  @Transactional
  public void delete(int boardNo) throws Exception {
    boardDao.delete(boardNo);
  }

}
