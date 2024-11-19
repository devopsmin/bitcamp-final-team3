package project.tripMaker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.tripMaker.service.CommentService;
import project.tripMaker.service.CompanionRecruitService;
import project.tripMaker.service.CompanionService;
import project.tripMaker.service.ScheduleService;
import project.tripMaker.vo.*;

import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Controller
@RequestMapping("/companion")
public class CompanionController {

  private static final Logger logger = LoggerFactory.getLogger(CompanionController.class);

  private final CompanionService companionService;
  private final CommentService commentService;
  private final ScheduleService scheduleService;
  private final CompanionRecruitService companionRecruitService;

  private static final int BOARD_TYPE_COMPANION = 2;


  @GetMapping("list")
  public String list(
          @RequestParam(defaultValue = "1") int pageNo,
          @RequestParam(defaultValue = "10") int pageSize,
          Model model,
          @RequestParam(required = false, defaultValue = "order") String sort
  ) throws Exception {

    String order;

    switch (sort) {
      case "likes":
        order = "like";
        break;
      case "favorites":
        order = "favorite";
        break;
      case "views":
        order = "count";
        break;
      default:
        order = "order";
        break;
    }

    if (pageNo < 1) {
      pageNo = 1;
    }

    int length = companionService.countAll(BOARD_TYPE_COMPANION);

    int pageCount = length / pageSize;
    if (length % pageSize > 0) {
      pageCount++;
    }

    if (pageNo > pageCount) {
      pageNo = pageCount;
    }

    System.out.printf("order 값 : %s", order);


    List<Board> list = companionService.list(pageNo, pageSize, order);
    model.addAttribute("list", list);
    model.addAttribute("pageNo", pageNo);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("pageCount", pageCount);
    model.addAttribute("order", order);

    return "companion/list";
  }


  // 동행게시판 - 글쓰기
  @GetMapping("form")
  public String form(Model model, HttpSession session) throws Exception {

    // Login 세션 처리 - 로그인 여부 확인 및 사용자 고유번호 추출
    User loginUser = (User) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new Exception("로그인이 필요합니다.");
    }

    // 로그인 사용자가 등록한 여행일정 리스트 데이터 추출
    List<Trip> tripList = scheduleService.getTripsByUserNo(loginUser.getUserNo());
    logger.info("Original Trip List: {}", tripList);

    // board 테이블의 trip_no 값 추출
    List<Integer> registeredTripNos = companionService.getRegisteredTripNos();
    logger.info("Registered Trip Numbers: {}", registeredTripNos);

    // board 테이블의 trip_no 값과 tripList 의 trip_no 값 비교
    // 최종적으로 board 테이블에 등록 안된 trip_no 값을 가진 tripList 생성
    List<Trip> filteredTripList = tripList.stream()
            .filter(trip -> !registeredTripNos.contains(trip.getTripNo()))
            .collect(Collectors.toList());
    logger.info("Filtered Trip List: {}", filteredTripList);

    model.addAttribute("tripList", filteredTripList);
    return "companion/form";
    
  }


  // trip_no 값으로 schedule 정보 데이터 추출(form.html 데이터 요청 처리 목적)
  @PostMapping("selectSchedule")
  @ResponseBody
  public List<Schedule> selectSchedule(@RequestParam int tripNo) throws Exception {
    List<Schedule> scheduleList = scheduleService.getSchedulesByTripNo(tripNo);
    logger.info("Schedule List: {}", scheduleList);
    return scheduleList;

  }


  // 동행게시판 - 게시글 데이터 등록(일반 게시물 데이터, 동행모집 데이터 처리)
  @PostMapping("add")
  @ResponseBody
  public void add(@RequestBody AddRequest addRequest, HttpSession session) throws Exception {

    User loginUser = (User) session.getAttribute("loginUser");

    if (loginUser == null) {
      throw new Exception("로그인이 필요합니다.");
    }

    // 동행 데이터(일반 게시물 데이터) 처리 - Thymeleaf 으로 부터 받은 복합 객체중 Board 객체 추출
    Board board = addRequest.getBoard();

    // 동행 데이터(일반 게시물 데이터) 처리 - board 테이블 저장을 위한 userNo 저장
    board.setUserNo(loginUser.getUserNo());

    // 동행 데이터(일반 게시물 데이터) 처리 - board 테이블 저장을 위한 tripNo 저장
    int tripNo = board.getTripNo();

    // 동행 데이터(일반 게시물 데이터) 처리 - board 테이블 데이터 저장 시작
    companionService.add(board);
    
    // 동행 데이터(동행모집 데이터) 처리 -  Long 타입의 임의의 변수에 로그인한 사용자 번호 저장
    int userNo = board.getUserNo().intValue();

    // 동행 데이터(동행모집 데이터) 처리 - 반환값(직전 저장 게시물 데이터) 저장
    Board findBoard = companionService.selectIdNoByTripNo(tripNo, userNo);

    // 동행 데이터(동행모집 데이터) 처리 - 데이터 추출(직전 저장 게시물의 게시물 번호)
    int findBoardId = findBoard.getBoardNo();

    // 동행 데이터(동행모집 데이터) 처리 - 동행 모집 데이터 저장
    List<Companionrecruit> companionRecruits = addRequest.getCompanionRecruits();
    for (Companionrecruit recruit : companionRecruits) {
      recruit.setBoardNo(findBoardId);
      companionRecruitService.addRecruit(recruit);
    }
    
  }


  // 동행게시판 - 상세 글조회
  @GetMapping("view")
  public String view(@RequestParam int boardNo, Model model) throws Exception {

    // 게시글 조회수 증가 처리
    companionService.increaseViewCount(boardNo);

    // 게시글 데이터 로드
    Board board = companionService.findBy(boardNo);

    // 여행일정(Schedule) 데이터 추출 : trip_no 값 추출(특정 Schedule List 데이터 추출시 필요)
    int tripNo = board.getTripNo();
    // 여행일정(Schedule) 데이터 추출 : schedule List 데이터 추출(scheduleService viewSchedule 메서드를 이용)
    List<Schedule> scheduleList = scheduleService.viewSchedule(tripNo);
    // 여행일정(Schedule) 데이터 추출 : schedule List 데이터 확인(디버그용)
    logger.info("schedule List 의 데이터: {}", scheduleList);
    // 여행일정(Schedule) 데이터 추출 : view.html 속성 추가로 List 전달(view.html 에서 scheduleList 명칭으로 사용)
    model.addAttribute("scheduleList", scheduleList);
    
    
    // 동행모집(CompanionRecruit) 데이터 추출 : boardNo 값 이용
    List<Companionrecruit> companionrecruitList = companionRecruitService.findRecruitByRecruit(boardNo);
    // 동행모집(CompanionRecruit) 데이터 추출 : companionrecruitList 데이터 확인(디버그용)
    logger.info("companionList 의 데이터: {}", companionrecruitList);
    // 여행일정(Schedule) 데이터 추출 : view.html 속성 추가로 List 전달(view.html 에서 companionrecruitList 명칭으로 사용)
    model.addAttribute("companionrecruitList", companionrecruitList);
    
    // 동행 게시글이 존재하지 않을때 처리
    if (board == null) {
      throw new Exception("게시글이 존재하지 않습니다.");
    }

    // 동행게시물이 존재할때 board 데이터 속성 추가
    model.addAttribute("board", board);

    return "companion/view";

  }

  @PostMapping("modify")
  public String modify(@RequestParam("boardNo") int boardNo, Model model, HttpSession session) throws Exception {
    Board board = companionService.findBy(boardNo);

    if (board == null) {
      throw new Exception("게시글이 존재하지 않습니다.");
    }

    model.addAttribute("board", board);
    return "companion/modify";
  }

  @PostMapping("update")
  public String update(
          @RequestParam("boardNo") int boardNo,
          @RequestParam("boardTitle") String boardTitle,
          @RequestParam("boardContent") String boardContent,
          @RequestParam("boardTag") String boardTag
  ) throws Exception {
    Board board = companionService.findBy(boardNo);
    if (board == null) {
      throw new Exception("없는 게시글입니다.");
    }

    board.setBoardTitle(boardTitle);
    board.setBoardContent(boardContent);
    board.setBoardTag(boardTag);

    companionService.update(board);
    return "redirect:view?boardNo=" + boardNo;
  }

  @GetMapping("delete")
  public String delete(int boardNo, HttpSession session) throws Exception {
    User loginUser = (User) session.getAttribute("loginUser");
    int sessionUserNo = loginUser.getUserNo().intValue();
    Board board = companionService.findBy(boardNo);
    int userno = board.getUserNo().intValue();

    if (board == null) {
      throw new Exception("없는 게시글입니다.");
    }

    companionService.delete(boardNo);
    if (sessionUserNo == userno) {
      companionService.delete(boardNo);
    } else {
      throw new Exception("삭제 권한이 없습니다.");
    }
    return "redirect:list";
  }

  @GetMapping("search")
  public String search(
          @RequestParam("query") String query,
          @RequestParam(defaultValue = "1") int pageNo,
          @RequestParam(defaultValue = "10") int pageSize,
          Model model,
          @RequestParam(value = "option", required = false, defaultValue = "title") String option
  ) throws Exception {
    String decodeQuery = URLDecoder.decode(query, StandardCharsets.UTF_8);
    List<Board> list;
    String type;

    switch (option) {
      case "title":
        type = "title";
        break;
      case "writer":
        type = "writer";
        break;
      case "city":
        type = "city";
        break;
      case "tag":
        type = "tag";
        break;
      case "themaName":
        type = "thema";
        break;
      default:
        type = "";
        break;
    }
    if (pageNo < 1) {
      pageNo = 1;
    }

    int length = companionService.countAll(BOARD_TYPE_COMPANION);

    int pageCount = length / pageSize;
    if (length % pageSize > 0) {
      pageCount++;
    }

    if (pageNo > pageCount) {
      pageNo = pageCount;
    }

    list = companionService.searchlist(pageNo, pageSize, type, decodeQuery);
    model.addAttribute("list", list);
    model.addAttribute("pageNo", pageNo);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("pageCount", pageCount);
    model.addAttribute("option", option);
    model.addAttribute("type", type);
    model.addAttribute("query", decodeQuery);

    return "companion/list";
  }
}
