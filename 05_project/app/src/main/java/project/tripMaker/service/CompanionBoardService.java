package project.tripMaker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.tripMaker.dao.BoardDao;
import project.tripMaker.vo.Board;

import java.util.List;

@Service
public class CompanionBoardService {


  private BoardDao boardDao;

  public CompanionBoardService(BoardDao boardDao) {
    this.boardDao = boardDao;
  }

  // 게시글 작성
  @Transactional
  public void add(Board board) throws Exception {
    boardDao.insert(board);
  }

  // 게시글 목록 조회
  public List<Board> list() throws Exception {
    return boardDao.list();
  }

  // 특정 게시글 조회
  public Board findBy(int boardNo) throws Exception {
    return boardDao.findBy(boardNo);
  }

  public void increaseViewCount(int boardNo) throws Exception {
    Board board = boardDao.findBy(boardNo);
    if (board == null) {
      boardDao.updateViewCount(boardNo, board.getBoardCount() + 1);
    }
  }

  // 게시글 수정
  @Transactional
  public boolean update(Board board) throws Exception {
      return boardDao.update(board);
  }

  // 게시글 삭제
  @Transactional
  public void delete(int boardNo) throws Exception {
    boardDao.delete(boardNo);
  }
}


