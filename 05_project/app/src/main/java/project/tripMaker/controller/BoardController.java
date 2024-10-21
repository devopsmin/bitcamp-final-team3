package project.tripMaker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.tripMaker.service.BoardService;
import project.tripMaker.service.CommentService;
import project.tripMaker.vo.Board;

import java.util.List;
import project.tripMaker.vo.Comment;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

  private final BoardService boardService;
  private final CommentService commentService;

  @GetMapping("list")
  public String list(
      @RequestParam(defaultValue = "1") int pageNo,
      @RequestParam(defaultValue = "5") int pageSize,
      Model model,
      @RequestParam(required = false, defaultValue = "latest") String sort
  ) throws Exception {

    List<Board> boardList;

    switch (sort){

      case "likes":
        boardList = boardService.listByLikes(pageNo, pageSize);
        break;
      case "favorites":
        boardList = boardService.listByFavorites(pageNo, pageSize);
        break;
      case "views":
        boardList = boardService.listByViews(pageNo, pageSize);
        break;
      default: //"latest"
        boardList = boardService.list(pageNo, pageSize);
        break;
    }

    if (pageNo < 1) {
      pageNo = 1;
    }

    int length = boardService.countAll();

    int pageCount = length / pageSize;
    if (length % pageSize > 0) {
      pageCount++;
    }

    if (pageNo > pageCount) {
      pageNo = pageCount;
    }

    model.addAttribute("list", boardList);
    model.addAttribute("pageNo", pageNo);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("pageCount", pageCount);

    return "board/list";
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

    List<Comment> commentList = commentService.list(boardNo);

    model.addAttribute("board", board);
    model.addAttribute("commentList", commentList);
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

  @PostMapping("update")
  public String update(
      @RequestParam("no") Integer boardNo,
      @RequestParam("title") String title,
      @RequestParam("content") String content) throws Exception {

    Board board = boardService.get(boardNo);
    board.setBoardTitle(title);
    board.setBoardContent(content);

    boardService.update(board);
    return "redirect:view?boardNo=" + boardNo;
  }

  @PostMapping("modify")
  public String modify(@RequestParam("no") int boardNo, Model model) throws Exception {
    Board board = boardService.get(boardNo);

    if (board == null) {
      throw new Exception("게시글이 존재하지 않습니다.");
    }
    model.addAttribute("board", board);
    return "board/modify";
  }
}
