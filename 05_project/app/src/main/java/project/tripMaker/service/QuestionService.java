package project.tripMaker.service;

import java.util.Date;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.tripMaker.dao.BoardQuestionDao;
import project.tripMaker.dao.CommentDao;
import project.tripMaker.vo.Board;
import project.tripMaker.vo.Trip;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class QuestionService implements BoardService {

  private final BoardQuestionDao boardQuestionDao;
  private final CommentDao commentDao;


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


  public List<Board> listBoard(int boardtypeNo) throws Exception {
    return boardQuestionDao.listBoard(boardtypeNo);
  }

//  @Override
//  public int countAll(int BOARD_TYPE_REVIEW) throws Exception {
//    return 0;
//  }

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

  public int getCommentCount(int boardNo) throws Exception {
    return commentDao.countByBoardNo(boardNo);
  }

  public int getLikeCount(int boardNo) throws Exception {
    Integer likeCount = boardQuestionDao.getLikeCount(boardNo);
    return (likeCount != null) ? likeCount : 0;
  }

  // 좋아요 여부 확인
  public boolean isLiked(Map<String, Object> params) throws Exception {
    return boardQuestionDao.isLiked(params);
  }

  // 좋아요 추가
  @Transactional
  public void addLike(Map<String, Object> params) throws Exception {
    boardQuestionDao.addLike(params);
  }

  // 좋아요 삭제
  @Transactional
  public void removeLike(Map<String, Object> params) throws Exception {
    boardQuestionDao.removeLike(params);
  }

  // 즐겨찾기 수 조회
  public int getFavorCount(int boardNo) throws Exception {
    Integer favorCount = boardQuestionDao.getFavorCount(boardNo);
    return (favorCount != null) ? favorCount : 0;
  }

  // 즐겨찾기 여부 확인
  public boolean isFavored(Map<String, Object> params) throws Exception {
    return boardQuestionDao.isFavored(params);
  }

  // 즐겨찾기 추가
  @Transactional
  public void addFavor(Map<String, Object> params) throws Exception {
    boardQuestionDao.addFavor(params);
  }

  // 즐겨찾기 삭제
  @Transactional
  public void removeFavor(Map<String, Object> params) throws Exception {
    boardQuestionDao.removeFavor(params);
  }

  @Transactional
  public Map<String, Object> toggleLike(int boardNo, int userNo) throws Exception {
    Map<String, Object> params = new HashMap<>();
    params.put("boardNo", boardNo);
    params.put("userNo", userNo);

    Map<String, Object> response = new HashMap<>();
    if (isLiked(params)) {
      removeLike(params);
      response.put("isLiked", false);
      response.put("message", "좋아요가 취소되었습니다.");
    } else {
      addLike(params);
      response.put("isLiked", true);
      response.put("message", "좋아요가 추가되었습니다.");
    }
    response.put("likeCount", getLikeCount(boardNo));
    return response;
  }

  // 즐겨찾기 토글 메서드
  @Transactional
  public Map<String, Object> toggleFavor(int boardNo, int userNo) throws Exception {
    Map<String, Object> params = new HashMap<>();
    params.put("boardNo", boardNo);
    params.put("userNo", userNo);

    Map<String, Object> response = new HashMap<>();
    if (isFavored(params)) {
      removeFavor(params);
      response.put("isFavored", false);
      response.put("message", "즐겨찾기가 취소되었습니다.");
    } else {
      addFavor(params);
      response.put("isFavored", true);
      response.put("message", "즐겨찾기가 추가되었습니다.");
    }
    response.put("favorCount", getFavorCount(boardNo));
    return response;
  }

  public List<Board> getTopRecommendedBoards(int boardtypeNo, int limit) throws Exception {
    // 모든 게시글을 가져오고, 추천 점수를 계산하여 상위 N개의 게시글을 반환
    List<Board> allBoards = listBoard(boardtypeNo); // 전체 게시글 조회
    allBoards.sort((b1, b2) -> Double.compare(calculateRecommendationScore(b2), calculateRecommendationScore(b1)));
    // 람다식으로 두 객체 비교, b2kr b1보다 높으면 음수를 반환, 낮으면 양수를 반환하여 내림차순으로 정렬

    return allBoards.stream().limit(limit).toList();
    // 리스트를 stream으로 변환한 후 limit 만큼 상위 n개를 리스트 형태로 변환하여 반환
  }

  // 게시글의 추천 점수를 계산하는 메서드
  private double calculateRecommendationScore(Board board) {
    double viewWeight = 0.4;  // 조회수 점수 할당
    double likeWeight = 0.3; // 좋아요 점수 할당
    double favorWeight = 0.2; // 즐겨찾기 점수 할당
    double recencyWeight = 0.1; // 최근글 점수 할당

    double recencyScore = calculateRecencyScore(board.getBoardCreatedDate()); // Date 사용하여 최근성 점수로 계산

    return (board.getBoardCount() * viewWeight) +
            (board.getBoardLike() * likeWeight) +
            (board.getBoardFavor() * favorWeight) +
            (recencyScore * recencyWeight);
  }

  // Date 객체를 기반으로 최근성 점수를 계산하는 메서드
  private int calculateRecencyScore(Date boardCreatedDate) {
    Date now = new Date();
    long diffInMillies = now.getTime() - boardCreatedDate.getTime(); // 현재 날짜 - 게시글 날짜, 밀리초 단위로 계산된 값
    long daysAgo = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS); // 밀리초를 일(day) 단위로 변환한 후 daysAgo에 저장

    if (daysAgo <= 7) return 10;       // 최근 일주일 이내
    else if (daysAgo <= 30) return 7;  // 한 달 이내
    else return 3;                     // 한 달 이상 지난 게시글
  }

  // Trip리스트 저장
  public List<Trip> getTripsByBoardNo(int boardNo) {
    return boardQuestionDao.findTripsByBoardNo(boardNo);
  }

  public List<Board> searchBoards(String search, int pageNo, int pageSize) throws Exception {

    int rowNo = (pageNo - 1) * pageSize;
    int length = pageSize;

    Map<String, Object> params = new HashMap<>();

    params.put("search", search);
    params.put("rowNo", rowNo);
    params.put("length", length);
    return boardQuestionDao.searchBoards(params);
  }

  public int searchBoardCount(String search) throws Exception {
    return boardQuestionDao.searchBoardCount(search);
  }

  @Override
  public int countAll(int BOARD_TYPE_REVIEW) throws Exception {
    return 0;
  }

  public List<Board> listUser(Long userNo) throws Exception {
    return boardQuestionDao.listUser(userNo);
  }
}
