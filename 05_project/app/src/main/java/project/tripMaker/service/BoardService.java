package project.tripMaker.service;

import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.tripMaker.vo.Board;
import project.tripMaker.dao.BoardDao;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

  private final BoardDao boardDao;

  public List<Board> list(int pageNo, int pageSize) throws Exception {

    HashMap<String, Object> options = new HashMap<>();
    options.put("rowNo", (pageNo - 1) * pageSize);
    options.put("length", pageSize);

    return boardDao.list(options);
  }

  public int countAll() throws Exception {
    return boardDao.countAll();
  }

  public List<Board> listByLikes() {
    return boardDao.listLike();
  }

  public List<Board> listByFavorites() {
    return boardDao.listFavor();
  }

  public List<Board> listByViews() {
    return boardDao.listView();
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
