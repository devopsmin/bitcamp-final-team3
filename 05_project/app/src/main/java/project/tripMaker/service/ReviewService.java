package project.tripMaker.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.tripMaker.dao.BoardDao;
import project.tripMaker.dao.BoardReviewDao;
import project.tripMaker.dao.CommentDao;
import project.tripMaker.vo.Board;
import project.tripMaker.vo.Trip;

@Service
@RequiredArgsConstructor
public class ReviewService implements BoardService {

  private final BoardReviewDao boardReviewDao;
  private final CommentDao commentDao;
  private static final int BOARD_TYPE_REVIEW = 3; // 리뷰용 게시판 타입

  //   list
  //   1. 최신순   list [Default]
  //   2. 좋아요   Likes
  //   3. 즐겨찾기 Favor
  //   4. 조회수   View
  public List<Board> list(int pageNo, int pageSize, int BOARD_TYPE_REVIEW) throws Exception {
    HashMap<String, Object> options = new HashMap<>();
    options.put("rowNo", (pageNo - 1) * pageSize);
    options.put("length", pageSize);
    options.put("boardtypeNo", BOARD_TYPE_REVIEW);

    return boardReviewDao.list(options);
  }

  public List<Board> listByLikes(int pageNo, int pageSize, int BOARD_TYPE_REVIEW) throws Exception {
    HashMap<String, Object> options = new HashMap<>();
    options.put("rowNo", (pageNo - 1) * pageSize);
    options.put("length", pageSize);
    options.put("boardtypeNo", BOARD_TYPE_REVIEW);

    return boardReviewDao.listLike(options);
  }

  public List<Board> listByFavorites(int pageNo, int pageSize, int BOARD_TYPE_REVIEW) throws Exception {
    HashMap<String, Object> options = new HashMap<>();
    options.put("rowNo", (pageNo - 1) * pageSize);
    options.put("length", pageSize);
    options.put("boardtypeNo", BOARD_TYPE_REVIEW);

    return boardReviewDao.listFavor(options);
  }

  public List<Board> listByViews(int pageNo, int pageSize, int BOARD_TYPE_REVIEW) throws Exception {
    HashMap<String, Object> options = new HashMap<>();
    options.put("rowNo", (pageNo - 1) * pageSize);
    options.put("length", pageSize);
    options.put("boardtypeNo", BOARD_TYPE_REVIEW);

    return boardReviewDao.listView(options);
  }

  // Review 전체 RAW 수 처리
  public int countAll(int BOARD_TYPE_REVIEW) throws Exception {
    return boardReviewDao.countAll(BOARD_TYPE_REVIEW);
  }

  // 게시글 쓰기
  @Transactional
  public void add(Board board) throws Exception {
    board.setBoardtypeNo(BOARD_TYPE_REVIEW); // 게시판 타입 설정
    boardReviewDao.insert(board);
  }

  // 게시글 찾기
  public Board get(int boardNo) throws Exception {
    return boardReviewDao.findBy(boardNo);
  }

  // 게시글 삭제
  @Transactional
  public void delete(int boardNo) throws Exception {
    boardReviewDao.delete(boardNo);
  }

  // 게시글 수정
  @Transactional
  public boolean update(Board board) throws Exception {
    return boardReviewDao.update(board);
  }

  // 조회수 증가
  @Transactional
  public void increaseBoardCount(int boardNo) throws Exception {
    Board board = boardReviewDao.findBy(boardNo);
    if (board != null) {
      boardReviewDao.updateViewCount(board.getBoardNo(), board.getBoardCount());
    }
  }

  // Trip리스트 저장
  public List<Trip> getTripsByBoardNo(int boardNo) {
    return boardReviewDao.findTripsByBoardNo(boardNo);
  }

  // 좋아요 실행
  public boolean toggleLike(int boardNo, Long userNo) {
    Map<String, Object> params = new HashMap<>();
    params.put("boardNo", boardNo);
    params.put("userNo", userNo);

    if (boardReviewDao.isLiked(params)) {
      boardReviewDao.removeLike(params);
      return false; // 좋아요 취소됨
    } else {
      boardReviewDao.addLike(params);
      return true; // 좋아요 추가됨
    }
  }

  // 좋아요 갯수 확인
  public int getLikeCount(int boardNo) {
    return boardReviewDao.getLikeCount(boardNo);
  }

  // 좋아요 여부 확인
  public boolean isLiked(int boardNo, Long userNo) {
    Map<String, Object> params = new HashMap<>();
    params.put("boardNo", boardNo);
    params.put("userNo", userNo);
    return boardReviewDao.isLiked(params);  // 좋아요 여부 확인
  }

  // 즐겨찾기 실행
  public boolean toggleFavor(int boardNo, Long userNo) {
    Map<String, Object> favorParams = new HashMap<>();
    favorParams.put("boardNo", boardNo);
    favorParams.put("userNo", userNo);

    if (boardReviewDao.isFavored(favorParams)) {
      boardReviewDao.removeFavor(favorParams);
      return false; // 즐겨찾기 취소됨
    } else {
      boardReviewDao.addFavor(favorParams);
      return true; // 즐겨찾기 추가됨
    }
  }

  // 즐겨찾기 갯수 확인
  public int getFavorCount(int boardNo) {
    return boardReviewDao.getFavorCount(boardNo);
  }

  // 즐겨찾기 여부 확인
  public boolean isFavored(int boardNo, Long userNo) {
    Map<String, Object> favorParams = new HashMap<>();
    favorParams.put("boardNo", boardNo);
    favorParams.put("userNo", userNo);
    return boardReviewDao.isLiked(favorParams);  // 즐겨찾기 여부 확인
  }

  // Search
  // 1. 제목   Title [Default]
  // 2. 작성자 Writer
  // 3. 시도   City
  // 4. 태그   Tag
  public List<Board> findByTitle(String title) {
    return boardReviewDao.findByTitle(title);
  }

  public List<Board> findByWriter(String writer) {
    return boardReviewDao.findByWriter(writer);
  }

  public List<Board> findByCity(String city) {
    return boardReviewDao.findByCity(city);
  }

  public List<Board> findByTag(String tag) {
    return boardReviewDao.findByTag(tag);
  }

  // 댓글 수
  public int getCommentCount(int boardNo) throws Exception {
    return commentDao.countByBoardNo(boardNo);
  }
}
