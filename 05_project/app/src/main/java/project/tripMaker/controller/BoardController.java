package project.tripMaker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import project.tripMaker.service.BoardService;
import project.tripMaker.vo.Board;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/board")
public class BoardController {

  private BoardService boardService;

  public BoardController(
      BoardService boardService) {
    this.boardService = boardService;

  }

  @GetMapping("list")
  public void list(Model model) throws Exception {
    List<Board> list = boardService.list();
    for ( Board board : list ) {
      System.out.println(board.toString());
    }
    model.addAttribute("list", list);
  }

  @GetMapping("form")
  public void form(){
  }

  @PostMapping("add")
  public String add(Board board) throws Exception {

    boardService.add(board);
    return "redirect:list";
  }

  @GetMapping("view")
  public void view(int boardNo, Model model) throws Exception {
    Board board = boardService.get(boardNo);
    if (board == null) {
      throw new Exception("게시글이 존재하지 않습니다.");
    }

    boardService.increaseViewCount(board.getBoardNo());

    model.addAttribute("board", board);
  }

  @GetMapping("delete")
  public String delete(int boardNo) throws Exception {
    Board board = boardService.get(boardNo);

    if (board == null) {
      throw new Exception("없는 게시글입니다.");
    }

    boardService.delete(boardNo);
    return "redirect:list";
  }
}
