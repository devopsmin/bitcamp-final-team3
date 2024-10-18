package project.tripMaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import project.tripMaker.dao.BoardDao;
import project.tripMaker.vo.Board;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanionBoardService {

  @Autowired
  BoardDao boardDao;

  public List<Board> mainList() throws Exception {
    return boardDao.mainList();
  }

  public Board get(int boardNo) throws Exception {
    return boardDao.findBy(boardNo);
  }

  public void add(Board board) throws Exception {
    boardDao.insert(board);
  }

  public void increaseViewCount(int boardNo) throws Exception {
    Board board = boardDao.findBy(boardNo);
    if (board != null) {
      boardDao.updateViewCount(board.getBoardNo(), board.getViewCount() + 1);
    }
  }
}
