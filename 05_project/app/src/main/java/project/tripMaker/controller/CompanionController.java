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

    User loginUser = (User) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new Exception("로그인이 필요합니다.");
    }

    List<Trip> tripList = scheduleService.getTripsByUserNo(loginUser.getUserNo());
    logger.info("Trip List: {}", tripList);
    model.addAttribute("tripList", tripList);
    return "companion/form";
  }

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

    Board board = addRequest.getBoard();

    board.setUserNo(loginUser.getUserNo());

    // Step 3: tripNo 값을 임의의 int 형 변수에 저장
    int tripNo = board.getTripNo();

    // Step 4: companionService 의 add 메서드 호출
    companionService.add(board);

    // Step 5: Long 타입의 임의의 변수에 로그인한 사용자 번호 저장
    int userNo = board.getUserNo().intValue();

    // Step 6: 반환된 값 저장
    Board findBoard = companionService.selectIdNoByTripNo(tripNo, userNo);

    int findBoardId = findBoard.getBoardNo();

    System.out.println("userNo 값 : " + userNo);
    System.out.println("findBoardId 값 : " + findBoardId);

    // 동행 모집 정보 추가
    List<Companionrecruit> companionRecruits = addRequest.getCompanionRecruits();
    for (Companionrecruit recruit : companionRecruits) {
      recruit.setBoardNo(findBoardId);
      companionRecruitService.addRecruit(recruit);
    }
  }

  @GetMapping("view")
  public String view(@RequestParam int boardNo, Model model) throws Exception {
    companionService.increaseViewCount(boardNo);
    Board board = companionService.findBy(boardNo);

    if (board == null) {
      throw new Exception("게시글이 존재하지 않습니다.");
    }

    List<Comment> commentList = commentService.list(boardNo);
    model.addAttribute("board", board);
    model.addAttribute("commentList", commentList);

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
