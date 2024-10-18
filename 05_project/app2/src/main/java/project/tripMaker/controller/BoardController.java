package project.tripMaker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.tripMaker.service.BoardService;
import project.tripMaker.vo.Board;
import project.tripMaker.vo.Comment;

import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {

  @Autowired
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

  @PostMapping("view2")
  public void view2(int boardNo, Model model) throws Exception {

    Board board = boardService.get(boardNo);

    model.addAttribute("board", board);

  }

  @PostMapping("update")
  public String update(int boardNo, String boardTitle, String boardContent, String boardTag) throws Exception {

    Board board = boardService.get(boardNo);

    board.setBoardTitle(boardTitle);
    board.setBoardContent(boardContent);
    board.setBoardTag(boardTag);

    boardService.update(board);
    return "redirect:view?boardNo=" + boardNo;
  }

  @GetMapping("delete")
  public String delete(int boardNo) throws Exception {

    boardService.delete(boardNo);
    return "redirect:list";
  }

}
