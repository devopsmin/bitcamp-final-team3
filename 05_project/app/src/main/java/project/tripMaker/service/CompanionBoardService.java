package project.tripMaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import project.tripMaker.dao.BoardDao;
// import project.tripMaker.vo.AttachedFile;
import project.tripMaker.vo.Board;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanionBoardService implements BoardService {
  @Autowired
  private BoardDao boardDao;
  private PlatformTransactionManager txManager;

  public CompanionBoardService(BoardDao boardDao, PlatformTransactionManager txManager) {
    this.boardDao = boardDao;
    this.txManager = txManager;
  }

  @Transactional
  public void add(Board board) throws Exception {
    boardDao.insert(board);
  }
  @Transactional
  public List<Board> list() throws Exception {
    return boardDao.list();
  }
  @Transactional
  public Board get(int boardNo) throws Exception {
    return boardDao.findBy(boardNo);
  }

  @Transactional
  public void increaseViewCount(int boardNo) throws Exception {
    Board board = boardDao.findBy(boardNo);
    if (board != null) {
      boardDao.updateViewCount(board.getBoardNo(), board.getBoardCount() + 1);
    }
  }
}
