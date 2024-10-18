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

  public BoardController(BoardService boardService) {
    this.boardService = boardService;
  }

  @GetMapping("form")
  public void form() {
  }

  @PostMapping("add")
  public String add(
          Board board) throws Exception {

    boardService.add(board);

    return "redirect:list";
  }

  @GetMapping("list")
  public void list(Model model) throws Exception {

    List<Board> list = boardService.list();

    model.addAttribute("list", list);
  }

  @GetMapping("view")
  public void view(int boardNo, Model model) throws Exception {

    Board board = boardService.get(boardNo);

    boardService.increaseBoardCount(board.getBoardNo());

    model.addAttribute("board", board);
  }

  @PostMapping("update")
  public String update(int boardNo, String boardTitle, String boardContent) throws Exception {

    Board board = boardService.get(boardNo);

    board.setBoardTitle(boardTitle);
    board.setBoardContent(boardContent);

    boardService.update(board);
    return "redirect:list";
  }

  @GetMapping("delete")
  public String delete(int boardNo) throws Exception {

    boardService.delete(boardNo);
    return "redirect:list";
  }

}
