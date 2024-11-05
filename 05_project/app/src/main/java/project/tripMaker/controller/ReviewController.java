package project.tripMaker.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import project.tripMaker.service.CommentService;
import project.tripMaker.service.ReviewService;
import project.tripMaker.service.ScheduleService;
import project.tripMaker.vo.Board;
import project.tripMaker.vo.Comment;
import project.tripMaker.vo.Schedule;
import project.tripMaker.vo.Trip;
import project.tripMaker.vo.User;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewService reviewService;
  private final CommentService commentService;
  private final ScheduleService scheduleService;

  // ENUM : 리뷰용 게시판 타입
  private static final int BOARD_TYPE_REVIEW = 3;

  @GetMapping("list")
  public String list(
      // 페이지 설정
      // pageNo (페이지번호)      Default 1 = 1페이지
      // pageSize (화면출력 갯수) Default 8 = 8개의 게시글
      @RequestParam(defaultValue = "1") int pageNo,
      @RequestParam(defaultValue = "8") int pageSize,
      Model model,
      @RequestParam(required = false, defaultValue = "latest") String sort
  ) throws Exception {

    List<Board> boardList;

    // 게시물 목록 조회
    //   1. 최신      list [Default]
    //   2. 좋아요    Like
    //   3. 즐겨찾기  Favor
    //   4. 조회수    View
    switch (sort) {
      case "likes":
        boardList = reviewService.listByLikes(pageNo, pageSize, BOARD_TYPE_REVIEW);
        break;
      case "favorites":
        boardList = reviewService.listByFavorites(pageNo, pageSize, BOARD_TYPE_REVIEW);
        break;
      case "views":
        boardList = reviewService.listByViews(pageNo, pageSize, BOARD_TYPE_REVIEW);
        break;
      default:
        boardList = reviewService.list(pageNo, pageSize, BOARD_TYPE_REVIEW);
        break;
    }

    // 전체 갯수 확인
    // 페이지 처리
    int length = reviewService.countAll(BOARD_TYPE_REVIEW);
    int pageCount = length / pageSize;

    // 페이지 처리 최소 1 이상
    if (pageNo < 1) {
      pageNo = 1;
    }

    // 갯수가 size 초과시 페이지 1칸더
    if (length % pageSize > 0) {
      pageCount++;
    }

    // 페이지 처리 최대 PageCount 까지
    if (pageNo > pageCount) {
      pageNo = pageCount;
    }

    for (Board board : boardList) {
      int commentCount = reviewService.getCommentCount(board.getBoardNo());
      board.setCommentCount(commentCount); // 댓글 개수 설정
    }

    model.addAttribute("list", boardList);
    model.addAttribute("sort", sort);
    model.addAttribute("pageNo", pageNo);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("pageCount", pageCount);

    return "review/list";
  }

  @GetMapping("form")
  public String form(Model model, HttpSession session) throws Exception {
    // 로그인 유저 확인
    User loginUser = (User) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new Exception("로그인이 필요합니다.");
    }

    // 로그인 유저의 Trip List 찾아서 List 전달
    List<Trip> tripList = scheduleService.getTripsByUserNo(loginUser.getUserNo());
    model.addAttribute("trips", tripList);

    return "review/form";
  }

  @PostMapping("add")
  public String add(Board board, HttpSession session) throws Exception {
    // 로그인 유저 확인
    User loginUser = (User) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new Exception("로그인이 필요합니다.");
    }

    int tripNo = board.getTripNo();
    System.out.println(tripNo);

    board.setTripNo(tripNo);
    board.setWriter(loginUser);
    board.setBoardtypeNo(BOARD_TYPE_REVIEW); // 게시판 타입 설정
    reviewService.add(board);
    return "redirect:list";
  }

  @GetMapping("view")
  public String view(
      @RequestParam int boardNo,
      @SessionAttribute("loginUser") User user,
      Model model) throws Exception {

    Board board = reviewService.get(boardNo);
    if (board == null) {
      throw new Exception("게시글이 존재하지 않습니다.");
    }
    // 조회수 증가
    reviewService.increaseBoardCount(board.getBoardNo());
    // 작성자 확인
    User writer = board.getWriter();
    // 로그인 유저 확인
    Long userNo = user.getUserNo();
    // 좋아요 여부 확인
    boolean isLiked = reviewService.isLiked(boardNo, userNo);
    // 즐겨찾기 여부 확인
    boolean isFavored = reviewService.isFavored(boardNo, userNo);

    // 특정 Board의 Comment, Trip, Schedule 리스트 전달
    List<Comment> commentList = commentService.list(boardNo);
    List<Trip> tripList = reviewService.getTripsByBoardNo(board.getTripNo());
    List<Schedule> scheduleList = scheduleService.getSchedulesByTripNo(board.getTripNo());

    model.addAttribute("board", board);
    model.addAttribute("writer", writer);
    model.addAttribute("commentList", commentList);
    model.addAttribute("trip", tripList);
    model.addAttribute("schedule", scheduleList);
    model.addAttribute("isLiked", isLiked);
    model.addAttribute("isFavored", isFavored);
    return "review/view";
  }

  // 삭제
  @GetMapping("delete")
  public String delete(int boardNo) throws Exception {
    Board board = reviewService.get(boardNo);
    if (board == null) {
      throw new Exception("없는 게시글입니다.");
    }

    reviewService.delete(boardNo);
    return "redirect:list";
  }

  // 수정 페이지 정보전달
  @PostMapping("update")
  public String update(
      @RequestParam("no") Integer boardNo,
      @RequestParam("title") String title,
      @RequestParam("content") String content
  ) throws Exception {
    Board board = reviewService.get(boardNo);
    board.setBoardTitle(title);
    board.setBoardContent(content);

    reviewService.update(board);
    return "redirect:view?boardNo=" + boardNo;
  }

  // 수정 후 SQL Update 실행
  @PostMapping("modify")
  public String modify(@RequestParam("no") int boardNo, Model model) throws Exception {
    Board board = reviewService.get(boardNo);
    if (board == null) {
      throw new Exception("게시글이 존재하지 않습니다.");
    }
    User writer = board.getWriter();

    List<Trip> tripList = reviewService.getTripsByBoardNo(board.getTripNo());
    List<Schedule> scheduleList = scheduleService.getSchedulesByTripNo(board.getTripNo());

    model.addAttribute("board", board);
    model.addAttribute("writer", writer);
    model.addAttribute("trips", tripList);
    model.addAttribute("schedule", scheduleList);
    return "review/modify";
  }

  // 좋아요 실행
  @PostMapping("like/{boardNo}")
  @ResponseBody
  public Map<String, Object> toggleLike(
      @PathVariable int boardNo,
      @SessionAttribute("loginUser") User loginUser) {

    Long userNo = loginUser.getUserNo();
    boolean isLiked = reviewService.toggleLike(boardNo, userNo);
    int likeCount = reviewService.getLikeCount(boardNo);

    Map<String, Object> response = new HashMap<>();
    response.put("isLiked", isLiked);
    response.put("likeCount", likeCount);

    return response;
  }

  // 즐겨찾기 실행
  @PostMapping("favor/{boardNo}")
  @ResponseBody
  public Map<String, Object> toggleFavor(
      @PathVariable int boardNo,
      @SessionAttribute("loginUser") User loginUser) {

    Long userNo = loginUser.getUserNo();
    boolean isFavored = reviewService.toggleFavor(boardNo, userNo);
    int favorCount = reviewService.getFavorCount(boardNo);

    Map<String, Object> response = new HashMap<>();
    response.put("isFavored", isFavored);
    response.put("favorCount", favorCount);

    return response;
  }

  // user_no를 통해 trip_no 리스트를 가져오는 메서드
  @GetMapping("getTripList")
  public String getTripList(@RequestParam("userNo") Long userNo, Model model) {
    List<Integer> tripNos = scheduleService.getTripNosByUserNo(userNo);
    model.addAttribute("tripNos", tripNos);
    return "review/form"; // form에 리스트 표시
  }

  // 특정 trip_no를 통해 schedule 리스트를 가져오는 메서드
  @GetMapping("getScheduleList")
  @ResponseBody
  public List<Schedule> getScheduleList(@RequestParam("tripNo") int tripNo) {
    System.out.println(tripNo);
    return scheduleService.getSchedulesByTripNo(tripNo);
  }

  // 검색
  // 1. 제목   title
  // 2. 작성자 writer
  // 3. 시도   city
  // 4. 태그   tag
  @GetMapping("search")
  public String search(
      @RequestParam(value = "option", required = false, defaultValue = "title") String option,
      @RequestParam("query") String query,
      @RequestParam(defaultValue = "1") int pageNo,
      @RequestParam(defaultValue = "8") int pageSize,
      Model model
  ) {
    String decodedQuery = URLDecoder.decode(query, StandardCharsets.UTF_8);
    List<Board> searchResults;

    switch (option) {
      case "title":
        searchResults = reviewService.findByTitle(decodedQuery);
        break;
      case "writer":
        searchResults = reviewService.findByWriter(decodedQuery);
        break;
      case "city":
        searchResults = reviewService.findByCity(decodedQuery);
        break;
      case "tag":
        searchResults = reviewService.findByTag(decodedQuery);
        break;
      default:
        searchResults = List.of();
    }

    // 페이지 처리
    int totalResults = searchResults.size();
    int pageCount = (int) Math.ceil((double) totalResults / pageSize);

    // 시작 인덱스와 끝 인덱스 계산
    int startIndex = (pageNo - 1) * pageSize;
    int endIndex = Math.min(startIndex + pageSize, totalResults);

    // 페이지 번호에 따른 검색 결과 목록 슬라이싱
    List<Board> pagedResults = searchResults.subList(startIndex, endIndex);

    model.addAttribute("option", option);
    model.addAttribute("query", decodedQuery);
    model.addAttribute("list", pagedResults);
    model.addAttribute("pageNo", pageNo);  // 기본 페이지 번호
    model.addAttribute("pageSize", pageSize);  // 페이지 크기
    model.addAttribute("pageCount", pageCount);
    return "review/list"; // 게시글 목록을 출력하는 Thymeleaf 템플릿 파일 이름
  }
}
