package project.tripMaker.service;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.tripMaker.board.BoardType;
import project.tripMaker.dao.BoardCompanionDao;
import project.tripMaker.dao.CompanionRecruitDao;
import project.tripMaker.vo.Board;

import java.util.HashMap;
import java.util.List;

@Data
@Service
public class CompanionService {

  private final BoardCompanionDao boardCompanionDao;
  private final CompanionRecruitDao companionRecruitDao;
  private int BOARD_TYPE_COMPANION = BoardType.COMPANION.getTypeCode();

  // 게시글 작성
  @Transactional
  public void add(Board board) throws Exception {
    boardCompanionDao.insert(board);
  }

  // 게시글 목록 조회
  public List<Board> list(int pageNo, int pageSize, String order) throws Exception {

    HashMap<String, Object> options = new HashMap<>();
    options.put("rowNo", (pageNo - 1) * pageSize);
    options.put("length", pageSize);
    options.put("order", order);
    options.put("boardtype", BOARD_TYPE_COMPANION);

    return boardCompanionDao.list(options);
  }

  // 특정 게시글 조회
  public Board findBy(int boardNo) throws Exception {
    return boardCompanionDao.findBy(boardNo);
  }

  @Transactional
  public void increaseViewCount(int boardNo) throws Exception {
    Board board = boardCompanionDao.findBy(boardNo);
    if (board != null) {
      boardCompanionDao.updateViewCount(board.getBoardNo(), board.getBoardCount() + 1);
    }
  }

  public int countAll(int BOARD_TYPE_COMPANION) throws Exception {
    return boardCompanionDao.countAll(BOARD_TYPE_COMPANION);
  }

  // 게시글 수정
  @Transactional
  public boolean update(Board board) throws Exception {
    if (!boardCompanionDao.update(board)) {
      return false;
    }
    return true;
  }

  // 게시글 삭제
  @Transactional
  public void delete(int boardNo) throws Exception {
    companionRecruitDao.deleteCompanionRecruitByBoard(boardNo);
    boardCompanionDao.delete(boardNo);
  }

  public Board selectIdNoByTripNo(int tripNo, int userNo) throws Exception {
    return boardCompanionDao.selectIdNoByTripNo(tripNo, userNo);
  }

  public List<Integer> getRegisteredTripNos() throws Exception {
    return boardCompanionDao.getRegisteredTripNos();
  }

  public List<Board> searchlist(int pageNo, int pageSize, String type, String data) throws Exception{
    HashMap<String, Object> options = new HashMap<>();
    options.put("rowNo", (pageNo - 1) * pageSize);
    options.put("length", pageSize);
    options.put("type", type);
    options.put("data", data);
    options.put("boardtype", BOARD_TYPE_COMPANION);

    return boardCompanionDao.searchlist(options);
  }
}
