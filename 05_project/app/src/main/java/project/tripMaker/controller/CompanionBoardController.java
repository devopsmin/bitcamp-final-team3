package project.tripMaker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import project.tripMaker.service.CompanionBoardService;
import project.tripMaker.vo.Board;
import project.tripMaker.vo.User;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/companion")
@SessionAttributes({"companion", "companionList"})

public class CompanionBoardController {

  private CompanionBoardService companionBoardService;

  public CompanionBoardController(
          CompanionBoardService companionBoardService) {
    this.companionBoardService = companionBoardService;

  }

  @GetMapping("mainList")
  public String list(Model model) throws Exception {
    List<Board> mainlist = companionBoardService.mainList();
    for ( Board board : mainlist ) {
      System.out.println(board.toString());
    }
    model.addAttribute("mainList", mainlist);
    return "companion/mainList";
  }

  @GetMapping("view")
  public void view(int no, Model model) throws Exception {
    Board board = companionBoardService.get(no);
    if (board == null) {
      throw new Exception("게시글이 존재하지 않습니다.");
    }

    companionBoardService.increaseViewCount(board.getBoardNo());

    model.addAttribute("board", board);
  }

  public String add(
          Board board, HttpSession session) throws Exception {
    User loginUser = (User) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new Exception("로그인 하지 않았습니다.");
    }

    //board.setWriter(loginUser);
    return "redirect:list";
  }



}
