package project.tripMaker.service;

import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.tripMaker.dao.BoardQuestionDao;
import project.tripMaker.vo.Board;
import project.tripMaker.dao.BoardDao;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService implements BoardService {

  private final BoardQuestionDao boardQuestionDao;

  public List<Board> list(int pageNo, int pageSize) throws Exception {

    HashMap<String, Object> options = new HashMap<>();
    options.put("rowNo", (pageNo - 1) * pageSize);
    options.put("length", pageSize);

    return boardQuestionDao.list(options);
  }

  public List<Board> listByLikes(int pageNo, int pageSize) throws Exception {

    HashMap<String, Object> options = new HashMap<>();
    options.put("rowNo", (pageNo - 1) * pageSize);
    options.put("length", pageSize);

    return boardQuestionDao.listLike(options);
  }

  public List<Board> listByFavorites(int pageNo, int pageSize) throws Exception {

    HashMap<String, Object> options = new HashMap<>();
    options.put("rowNo", (pageNo - 1) * pageSize);
    options.put("length", pageSize);

    return boardQuestionDao.listFavor(options);
  }

  public List<Board> listByViews(int pageNo, int pageSize) throws Exception {

    HashMap<String, Object> options = new HashMap<>();
    options.put("rowNo", (pageNo - 1) * pageSize);
    options.put("length", pageSize);

    return boardQuestionDao.listView(options);
  }

  public int countAll() throws Exception {
    return boardQuestionDao.countAll();
  }

  @Transactional
  public void add(Board board) throws Exception {
    boardQuestionDao.insert(board);
  }

  public Board get(int boardNo) throws Exception {
    return boardQuestionDao.findBy(boardNo);
  }

  @Transactional
  public void increaseBoardCount(int boardNo) throws Exception {
    Board board = boardQuestionDao.findBy(boardNo);
    if (board != null) {
      boardQuestionDao.updateBoardCount(board.getBoardNo(), board.getBoardCount() + 1);
    }
  }

  @Transactional
  public boolean update(Board board) throws Exception {
    if (!boardQuestionDao.update(board)) {
      return false;
    }
    return true;
  }

  @Transactional
  public void delete(int boardNo) throws Exception {
    boardQuestionDao.delete(boardNo);
  }

  @Override
  public int countAll(int BOARD_TYPE_REVIEW) throws Exception {
    return 0;
  }
}
