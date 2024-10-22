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
}
