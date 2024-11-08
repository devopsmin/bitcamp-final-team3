package project.tripMaker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.tripMaker.service.QuestionService;
import project.tripMaker.service.CommentService;
import project.tripMaker.service.ScheduleService;
import project.tripMaker.service.UserService;
import project.tripMaker.vo.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

  private final QuestionService questionService;
  private final CommentService commentService;
  private final ScheduleService scheduleService;
  private final UserService userService;

  @GetMapping("form")
  public void form(Model model, HttpSession session) throws Exception {
    User loginUser = (User) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new Exception("로그인이 필요합니다.");
    }
//    User loginUser = new User();
//    loginUser.setUserNo(1L); // 강제로 userNo를 1로 설정

    List<Trip> tripList = scheduleService.getTripsByUserNo(loginUser.getUserNo());
    model.addAttribute("trips", tripList);

  }

  @PostMapping("add")
  public String add(
          Board board, HttpSession session) throws Exception {

    User loginUser = (User) session.getAttribute("loginUser");

    board.setWriter(loginUser);

    if ((board.getBoardTitle() == null || board.getBoardTitle().trim().isEmpty()) ||
            (board.getBoardContent() == null || board.getBoardContent().trim().isEmpty()) ||
            (board.getBoardTag() == null || board.getBoardTag().trim().isEmpty())) {

      throw new IllegalArgumentException(
              (board.getBoardTitle() == null || board.getBoardTitle().trim().isEmpty()) ? "제목을 입력해 주세요." :
                      (board.getBoardContent() == null || board.getBoardContent().trim().isEmpty()) ? "내용을 입력해 주세요." :
                              "태그를 입력해 주세요.");
    }
    int tripNo = board.getTripNo();

    board.setTripNo(tripNo);
    questionService.add(board);

    return "redirect:list";
  }

  @GetMapping("list")
  public String list(
          @RequestParam(defaultValue = "1") int pageNo,
          @RequestParam(defaultValue = "12") int pageSize,
          Model model,
          @RequestParam(required = false, defaultValue = "latest") String sort,
          @RequestParam(required = false) String search
  ) throws Exception {

    // 상위 4개의 베스트 게시글을 가져오기 위한 리스트
    List<Board> topBoards = questionService.getTopRecommendedBoards(1,4); // 베스트 게시글 상위 4개 가져오기

    List<Board> boardList;
    int totalBoardCount;

    if (search != null && !search.trim().isEmpty()) {
      boardList = questionService.searchBoards(search, pageNo, pageSize);
      totalBoardCount = questionService.searchBoardCount(search); // 검색된 게시글 수 계산
    } else {

      switch (sort) {

        case "likes":
          boardList = questionService.listByLikes(pageNo, pageSize);
          break;
        case "favorites":
          boardList = questionService.listByFavorites(pageNo, pageSize);
          break;
        case "views":
          boardList = questionService.listByViews(pageNo, pageSize);
          break;
        default: //"latest"
          boardList = questionService.list(pageNo, pageSize);
          break;
      }
      totalBoardCount = questionService.listBoard(1).size(); // 보드 타입 1에 대한 전체 게시물 수
    }

    for (Board board : boardList) {
      int commentCount = questionService.getCommentCount(board.getBoardNo());
      board.setCommentCount(commentCount); // 댓글 개수 설정
    }

    int pageCount = (int) Math.ceil((double) totalBoardCount / pageSize);

    if (pageNo < 1) {
      pageNo = 1;
    } else if (pageNo > pageCount) {
      pageNo = pageCount;
    }

    model.addAttribute("topBoards", topBoards);
    model.addAttribute("list", boardList);
    model.addAttribute("pageNo", pageNo);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("pageCount", pageCount);
    model.addAttribute("sort", sort);
    model.addAttribute("search", search);
    return "question/list";
  }

  @GetMapping("view")
  public void view(@RequestParam("boardNo") int boardNo,
                   @RequestParam(value = "page", defaultValue = "1") int page,
                   @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                   @RequestParam(value = "sort", defaultValue = "registered") String sort,
                   Model model,
                   HttpSession session) throws Exception {

    Board board = questionService.get(boardNo);
    if (board == null) {
      throw new Exception("게시글이 존재하지 않습니다.");
    }
//    System.out.println("===================="+board.toString());

    List<Comment> commentList;
    if ("likes".equals(sort)) {
      commentList = commentService.listByLikes(boardNo, page, pageSize);
    } else {
      commentList = commentService.listByPage(boardNo, page, pageSize);
    }

    List<Trip> tripList = questionService.getTripsByBoardNo(board.getTripNo());
    List<Schedule> scheduleList = scheduleService.getSchedulesByTripNo(board.getTripNo());

    int totalComments = commentService.list(boardNo).size();
    int totalPages = (int) Math.ceil((double) totalComments / pageSize);

    User loginUser = (User) session.getAttribute("loginUser");
    boolean isLoggedIn = loginUser != null;

    Map<Integer, Boolean> commentLikedMap = new HashMap<>();
    if (isLoggedIn) {
      for (Comment comment : commentList) {
        int commentLikeCount = commentService.getCommentLikeCount(comment.getCommentNo());
        comment.setCommentLike(commentLikeCount);

        Map<String, Object> params = new HashMap<>();
        params.put("commentNo", comment.getCommentNo());
        params.put("userNo", loginUser.getUserNo());
        boolean isCommentLiked = commentService.isCommentLiked(params);
        commentLikedMap.put(comment.getCommentNo(), isCommentLiked);
      }
    } else {
      // 비로그인 상태에서도 commentLikedMap을 초기화
      for (Comment comment : commentList) {
        commentLikedMap.put(comment.getCommentNo(), false);
      }
    }

    questionService.increaseBoardCount(board.getBoardNo());

    // 게시글 작성자와 로그인 사용자의 번호가 같은지 확인
    boolean isUserAuthorized = loginUser != null && loginUser.getUserNo() == board.getUserNo();

    boolean isLiked = false;
    boolean isFavored = false;

    if (isLoggedIn) {
      isLiked = questionService.isLiked(Map.of("boardNo", boardNo, "userNo", loginUser.getUserNo()));
      isFavored = questionService.isFavored(Map.of("boardNo", boardNo, "userNo", loginUser.getUserNo()));
    }

    // 좋아요 및 즐겨찾기 상태 확인
    int likeCount = questionService.getLikeCount(boardNo);
    int favorCount = questionService.getFavorCount(boardNo);

    Map<Integer, List<Schedule>> groupedSchedules = scheduleList.stream()
            .collect(Collectors.groupingBy(Schedule::getScheduleDay));

    model.addAttribute("trips", tripList);
    model.addAttribute("groupedSchedules", groupedSchedules);
    model.addAttribute("board", board);
    model.addAttribute("commentList", commentList);
    model.addAttribute("commentLikedMap", commentLikedMap);
    model.addAttribute("currentPage", page);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("totalPages", totalPages);
    model.addAttribute("sort", sort);


    model.addAttribute("isLiked", isLiked);
    model.addAttribute("isFavored", isFavored);
    model.addAttribute("isLoggedIn", isLoggedIn);
    model.addAttribute("loginUserNo", isLoggedIn ? loginUser.getUserNo() : null); // 로그인 유저의 번호 추가
    model.addAttribute("isUserAuthorized", isUserAuthorized);
    model.addAttribute("likeCount", likeCount);
    model.addAttribute("favorCount", favorCount);

  }

  @PostMapping("view2")
  public void view2(int boardNo, Model model, HttpSession session) throws Exception {

    User loginUser = (User) session.getAttribute("loginUser");

    Board board = questionService.get(boardNo);

    if (loginUser == null) {
      throw new Exception("로그인이 필요합니다.");
    }

    if (board == null) {
      throw new Exception("없는 게시글입니다.");
    } else if (loginUser.getUserNo() > 10 && board.getWriter().getUserNo() != loginUser.getUserNo()) {
      throw new Exception("변경 권한이 없습니다.");
    }

    List<Trip> tripList = questionService.getTripsByBoardNo(board.getTripNo());
    List<Schedule> scheduleList = scheduleService.getSchedulesByTripNo(board.getTripNo());

    model.addAttribute("trips", tripList);
    model.addAttribute("schedule", scheduleList);


    model.addAttribute("board", board);

  }

  @PostMapping("update")
  public String update(int boardNo, String boardTitle, String boardContent, String boardTag) throws Exception {

    Board board = questionService.get(boardNo);

    board.setBoardTitle(boardTitle);
    board.setBoardContent(boardContent);
    board.setBoardTag(boardTag);

    questionService.update(board);
    return "redirect:view?boardNo=" + boardNo;
  }

  @GetMapping("delete")
  public String delete(int boardNo, HttpSession session) throws Exception {

    User loginUser = (User) session.getAttribute("loginUser");

    Board board = questionService.get(boardNo);

    if (loginUser == null) {
      throw new Exception("로그인이 필요합니다.");
    }

    if (board == null) {
      throw new Exception("없는 게시글입니다.");
    } else if (loginUser.getUserNo() > 10 && board.getWriter().getUserNo() != loginUser.getUserNo()) {
      throw new Exception("삭제 권한이 없습니다.");
    }

    questionService.delete(boardNo);

    return "redirect:list";
  }

  @PostMapping("/toggleLike")
  @ResponseBody
  public Map<String, Object> toggleLike(@RequestParam("boardNo") int boardNo, HttpSession session) throws Exception {
    Map<String, Object> response = new HashMap<>();
    User loginUser = (User) session.getAttribute("loginUser");

    if (loginUser == null) {
      response.put("message", "로그인이 필요합니다.");
      response.put("isLiked", false); // 기본 좋아요 상태
      response.put("likeCount", questionService.getLikeCount(boardNo));
      return response;
    }

    try {
      response = questionService.toggleLike(boardNo, Math.toIntExact(loginUser.getUserNo()));
    } catch (Exception e) {
      response.put("message", "좋아요 처리 중 오류가 발생했습니다.");
    }

    return response;
  }

  @PostMapping("/toggleFavor")
  @ResponseBody
  public Map<String, Object> toggleFavor(@RequestParam("boardNo") int boardNo, HttpSession session) throws Exception {
    Map<String, Object> response = new HashMap<>();
    User loginUser = (User) session.getAttribute("loginUser");

    if (loginUser == null) {
      response.put("message", "로그인이 필요합니다.");
      response.put("isFavored", false); // 기본 즐겨찾기 상태
      response.put("favorCount", questionService.getFavorCount(boardNo));
      return response;
    }

    try {
      response = questionService.toggleFavor(boardNo, Math.toIntExact(loginUser.getUserNo()));
    } catch (Exception e) {
      response.put("message", "좋아요 처리 중 오류가 발생했습니다.");
    }
    return response;
  }

  // user_no를 통해 trip_no 리스트를 가져오는 메서드
  @GetMapping("getTripList")
  public String getTripList(@RequestParam("userNo") Long userNo, Model model) {
    List<Integer> tripNos = scheduleService.getTripNosByUserNo(userNo);
    model.addAttribute("tripNos", tripNos);
    return "question/form"; // form에 리스트 표시
  }

  // 특정 trip_no를 통해 schedule 리스트를 가져오는 메서드
  @GetMapping("getScheduleList")
  @ResponseBody
  public List<Schedule> getScheduleList(@RequestParam("tripNo") int tripNo) {
    System.out.println(tripNo);
    return scheduleService.getSchedulesByTripNo(tripNo);
  }

}
