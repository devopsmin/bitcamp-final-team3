package project.tripMaker.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.tripMaker.dao.BoardReviewDao;
import project.tripMaker.dao.CommentDao;
import project.tripMaker.dto.ReviewDto;
import project.tripMaker.vo.Board;
import project.tripMaker.vo.BoardImage;
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


    if (board.getBoardImages().size() > 0) {
      boardReviewDao.insertFiles(board);
    }
  }

  // 게시글 찾기
  public Board get(int boardNo) throws Exception {
    Board board = boardReviewDao.findBy(boardNo);

    if (board != null) {
      List<BoardImage> images = boardReviewDao.findImagesByBoardNo(boardNo); // 모든 이미지를 가져옵니다.
      System.out.println("Retrieved images: " + images); // 디버깅용 로그 출력
      board.setBoardImages(images); // Board 객체에 이미지 리스트 설정
    }
    return board;
    // return boardReviewDao.findBy(boardNo);
  }

  // 게시글 삭제
  @Transactional
  public void delete(int boardNo) throws Exception {
    boardReviewDao.deleteFiles(boardNo);
    boardReviewDao.delete(boardNo);
  }

  // 게시글 수정
  @Transactional
  public boolean update(Board board) throws Exception {
    if(board.getBoardImages().size() > 0){
      boardReviewDao.insertFiles(board);
    }

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
  // 5. 테마   Thema
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

  public List<Board> findByThema(String themaName) {
    return boardReviewDao.findByThema(themaName);
  }

  // 댓글 수
  public int getCommentCount(int boardNo) throws Exception {
    return commentDao.countByBoardNo(boardNo);
  }

  // 첨부파일 가져오기
  public BoardImage getAttachedFile(int fileNo) throws Exception {
    return boardReviewDao.getFile(fileNo);
  }

  // 첨부파일 삭제하기
  @Transactional
  public boolean deleteAttachedFile(int fileNo) throws Exception {
    if (!boardReviewDao.deleteFile(fileNo)) {
      return false;
    }
    return true;
  }

  // 게시글 관련 지울떄 싹다 지우기
  @Transactional 
  public void deleteAllFiles(int boardNo) throws Exception {
    boardReviewDao.deleteFiles(boardNo);
  }

  // 보드 게시글 리스트
  public List<Board> listBoard(int boardtypeNo) throws Exception {
    return boardReviewDao.listBoard(boardtypeNo);
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

  // 베스트 글 가져오기
  public List<Board> getTopRecommendedBoards(int boardtypeNo, int limit) throws Exception {
    // 모든 게시글을 가져오고, 추천 점수를 계산하여 상위 N개의 게시글을 반환
    List<Board> allBoards = listBoard(boardtypeNo); // 전체 게시글 조회
    allBoards.sort((b1, b2) -> Double.compare(calculateRecommendationScore(b2), calculateRecommendationScore(b1)));
    // 람다식으로 두 객체 비교, b2kr b1보다 높으면 음수를 반환, 낮으면 양수를 반환하여 내림차순으로 정렬

    return allBoards.stream().limit(limit).toList();
    // 리스트를 stream으로 변환한 후 limit 만큼 상위 n개를 리스트 형태로 변환하여 반환
  }

  public List<ReviewDto> getReviews(int page) throws Exception {
    int pageSize = 9;  // 한 페이지에 표시할 게시물 수를 설정합니다.
    List<Board> boards = list(page, pageSize, BOARD_TYPE_REVIEW);  // 페이지에 따른 게시글 리스트를 가져옵니다.

    // Board 객체를 ReviewDto로 변환하여 반환합니다.
    return boards.stream().map(board -> {
      LocalDate createdDate = board.getBoardCreatedDate().toInstant()
          .atZone(ZoneId.systemDefault())
          .toLocalDate();

      // Thema가 null일 경우 대비하여 삼항 연산자로 체크
      String themaName = board.getTrip().getThema() != null ? board.getTrip().getThema().getThemaName() : "No Thema";

      return ReviewDto.builder()
          .boardNo(board.getBoardNo())
          .boardTitle(board.getBoardTitle())
          .firstImageName(board.getFirstImageName() != null ? board.getFirstImageName() : "default.png")
          .cityName(board.getTrip().getCity().getCityName())
          .stateName(board.getTrip().getCity().getState().getStateName())
          .themaName(themaName)  // Thema 이름 설정
          .boardLike(board.getBoardLike())
          .boardFavor(board.getBoardFavor())
          .boardCount(board.getBoardCount())
          .writerNickname(board.getWriter().getUserNickname())
          .writerPhoto(board.getWriter().getUserPhoto())
          .boardCreatedDate(createdDate)  // LocalDate로 변환된 값 설정
          .build();
    }).collect(Collectors.toList());
  }


  // 마이페이지용 list
  public List<Board> listUser(Long userNo) throws Exception {
    return boardReviewDao.listUser(userNo);
  }

}
