package project.tripMaker.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.tripMaker.service.CommentService;
import project.tripMaker.service.CompanionService;
import project.tripMaker.service.ScheduleService;
import project.tripMaker.vo.Board;
import project.tripMaker.vo.Trip;
import project.tripMaker.vo.User;

import javax.servlet.http.HttpSession;
import java.util.List;

@Data
@Controller
@RequestMapping("/companion")
@RequiredArgsConstructor

public class CompanionController {

  private final CompanionService companionService;
  private final CommentService commentService;
  private final ScheduleService scheduleService;

  private static final int BOARD_TYPE_COMPANION = 2;

  // 동행게시판 - 게시글 목록 조회
  @GetMapping("list")
  public String list(
          @RequestParam(defaultValue = "1") int pageNo,
          @RequestParam(defaultValue = "5") int pageSize,
          Model model) throws Exception {

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

  // 동행게시판 - 게시글 작성 폼
  @GetMapping("form")
  public String form(Model model, HttpSession session) throws Exception {
    User loginUser = (User) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new Exception("로그인이 필요합니다.");
    }

      List<Trip> tripList = scheduleService.getTripsByUserNo(loginUser.getUserNo());
      model.addAttribute("trips", tripList);
      return "companion/form";
    }

  // 동행게시판 - 게시글 작성
  @PostMapping("add")
  public String add(Board board, HttpSession session) throws Exception {
    User loginUser = (User) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new Exception("로그인이 필요합니다.");
    }

    board.setWriter(loginUser);
    companionService.add(board);
    return "redirect:list";
  }

  // 동행게시판 - 특정 게시글 조회
  @GetMapping("view")
  public String view(int boardNo, Model model) throws Exception {
    Board board = companionService.get(boardNo);
    if (board == null) {
      throw new Exception("게시글이 존재하지 않습니다.");
    }

    companionService.increaseViewCount(board.getBoardNo());

    model.addAttribute("board", board);

    return "companion/view";
  }

  // 동행게시판 - 수정하기 페이지 로딩
  @PostMapping("modify")
  public String modify(@RequestParam("boardNo") int boardNo, Model model) throws Exception {
    Board board = companionService.get(boardNo);

    if (board == null) {
      throw new Exception("게시글이 존재하지 않습니다.");
    }
    model.addAttribute("board", board);
    return "companion/modify";
  }

  // 게시글 수정
  @PostMapping("update")
  public String update(
          @RequestParam("boardNo") int boardNo,
          @RequestParam("boardTitle")String boardTitle,
          @RequestParam("boardContent")String boardContent,
          @RequestParam("boardTag")String boardTag) throws Exception {

    Board board = companionService.get(boardNo);
    if (board == null) {
      throw new Exception("없는 게시글입니다.");
    }

    board.setBoardTitle(boardTitle);
    board.setBoardContent(boardContent);
    board.setBoardTag(boardTag);

    companionService.update(board);
    return "redirect:view?boardNo=" + boardNo;
  }

  // 게시글 삭제
  @GetMapping("delete")
  public String delete(int boardNo) throws Exception {
    Board board = companionService.get(boardNo);

    if (board == null) {
      throw new Exception("없는 게시글입니다.");
    }

    companionService.delete(boardNo);
    return "redirect:list";
  }
}
