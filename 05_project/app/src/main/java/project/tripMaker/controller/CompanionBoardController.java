package project.tripMaker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.tripMaker.service.CompanionBoardService;
import project.tripMaker.vo.Board;
import project.tripMaker.vo.User;

import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.util.List;

@Controller
@RequestMapping("/companionBoard")
public class CompanionBoardController {

  private CompanionBoardService companionBoardService;

  public CompanionBoardController(CompanionBoardService companionBoardService) {
    this.companionBoardService = companionBoardService;
  }


  // 게시글 작성 폼
  @GetMapping("form")
  public void form(){
  }

  // 게시글 작성
  @PostMapping("add")
  public String add(Board board) throws Exception{

    companionBoardService.add(board);
    return "redirect:list";
  }

  // 게시글 목록 조회
  @GetMapping("list")
  public String list(Model model) throws Exception {

    List<Board> list = companionBoardService.list();
    model.addAttribute("list", list);

    return "companionBoard/list";
  }

  // 특정 게시글 조회
  @GetMapping("view")
  public void view(int boardNo, Model model) throws Exception {
    Board board = companionBoardService.findBy(boardNo);
    if (board == null) {
      throw new Exception("게시글이 존재하지 않습니다.");
    }

    companionBoardService.increaseViewCount(board.getBoardNo());

    model.addAttribute("board", board);
  }

  // 게시글 수정
  @PostMapping("update")
  public String update(
          @RequestParam("boardNo") int boardNo,
          @RequestParam("boardTitle")String boardTitle,
          @RequestParam("boardContent")String boardContent,
          @RequestParam("boardTag")String boardTag) throws Exception {

    Board board = companionBoardService.findBy(boardNo);
    board.setBoardTitle(boardTitle);
    board.setBoardContent(boardContent);
    board.setBoardTag(boardTag);

    companionBoardService.update(board);
    return "redirect:view?boardNo=" + boardNo;
  }

  @PostMapping("modify")
  public String modify(@RequestParam("BoardNo") int boardNo, Model model) throws Exception {
    Board board = companionBoardService.findBy(boardNo);

    if (board == null) {
      throw new Exception("게시글이 존재하지 않습니다.");
    }
    model.addAttribute("board", board);
    return "companionBoard/modify";
  }

  // 게시글 삭제
  @GetMapping("delete")
  public String delete(int boardNo) throws Exception {
    Board board = companionBoardService.findBy(boardNo);

    if (board == null) {
      throw new Exception("없는 게시글입니다.");
    }

    companionBoardService.delete(boardNo);
    return "redirect:list";
  }
}