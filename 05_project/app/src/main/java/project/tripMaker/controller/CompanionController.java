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
          @RequestParam(defaultValue = "5") int pageSize,
          Model model
  ) throws Exception {

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

    List<Board> list = companionService.list(pageNo, pageSize);
    model.addAttribute("list", list);
    model.addAttribute("pageNo", pageNo);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("pageCount", pageCount);

    return "companion/list";
  }

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

  // trip_no 값으로 schedule 정보 데이터 추출
  @PostMapping("selectSchedule")
  @ResponseBody
  public List<Schedule> selectSchedule(@RequestParam int tripNo) throws Exception {
    List<Schedule> scheduleList = scheduleService.viewSchedule(tripNo);
    logger.info("Schedule List: {}", scheduleList);
    return scheduleList;

  }

  @PostMapping("add")
  @ResponseBody
  public void add(@RequestBody AddRequest addRequest, HttpSession session) throws Exception {

    User loginUser = (User) session.getAttribute("loginUser");

    if (loginUser == null) {
      throw new Exception("로그인이 필요합니다.");
    }

    // Thymeleaf 로 부터 받은 복합 객체중 Board 객체 추출
    Board board = addRequest.getBoard();

    // board 테이블 저장을 위한 userNo 저장
    board.setUserNo(loginUser.getUserNo());

    // board 테이블 저장을 위한 tripNo 저장
    int tripNo = board.getTripNo();

    // board 테이블 데이터 저장 시작
    companionService.add(board);

    // Step 5: Long 타입의 임의의 변수에 로그인한 사용자 번호 저장
    int userNo = board.getUserNo().intValue();

    // Step 6: 반환값 저장
    Board findBoard = companionService.selectIdNoByTripNo(tripNo, userNo);

    int findBoardId = findBoard.getBoardNo();

    System.out.println("userNo 값 : " + userNo);
    System.out.println("findBoardId 값 : " + findBoardId);

    // 동행 모집 정보 데이터 저장
    List<Companionrecruit> companionRecruits = addRequest.getCompanionRecruits();
    for (Companionrecruit recruit : companionRecruits) {
      recruit.setBoardNo(findBoardId);
      companionRecruitService.addRecruit(recruit);
    }
    
  }

  @GetMapping("view")
  public String view(@RequestParam int boardNo, Model model) throws Exception {

    // 게시글 조회수 증가 처리
    companionService.increaseViewCount(boardNo);
    
    // 게시글 데이터 로드
    Board board = companionService.findBy(boardNo);
    
    // 여행일정(Schedule) 데이터 추출 준비 : trip_no 값 추출
    int tripNo = board.getTripNo();

    List<Schedule> scheduleList = scheduleService.viewSchedule(tripNo);
    logger.info("Schedule List: {}", scheduleList);
    model.addAttribute("scheduleList", scheduleList);

    if (board == null) {
      throw new Exception("게시글이 존재하지 않습니다.");
    }

    model.addAttribute("board", board);

    return "companion/view";
  }

  @PostMapping("modify")
  public String modify(@RequestParam("boardNo") int boardNo, Model model) throws Exception {
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
  public String delete(int boardNo) throws Exception {
    Board board = companionService.findBy(boardNo);

    if (board == null) {
      throw new Exception("없는 게시글입니다.");
    }

    companionService.delete(boardNo);
    return "redirect:list";
  }
}
