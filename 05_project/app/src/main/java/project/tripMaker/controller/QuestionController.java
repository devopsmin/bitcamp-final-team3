package project.tripMaker.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.tripMaker.service.QuestionService;
import project.tripMaker.service.CommentService;
import project.tripMaker.service.ScheduleService;
import project.tripMaker.service.UserService;
import project.tripMaker.vo.Board;

import java.util.List;
import project.tripMaker.vo.Comment;
import project.tripMaker.vo.User;


@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

  private final QuestionService questionService;
  private final CommentService commentService;
  private final ScheduleService scheduleService;
  private final UserService userService;

  @GetMapping("form")
  public void form() {
  }

  @PostMapping("add")
  public String add(
          Board board) throws Exception {

    User user = userService.get((long)13);
    board.setWriter(user);
    questionService.add(board);

    return "redirect:list";
  }

  @GetMapping("list")
  public String list(
          @RequestParam(defaultValue = "1") int pageNo,
          @RequestParam(defaultValue = "7") int pageSize,
          Model model,
          @RequestParam(required = false, defaultValue = "latest") String sort
  ) throws Exception {

    List<Board> boardList;

    switch (sort){

      case "likes":
        boardList = questionService.listByLikes(pageNo, pageSize);
        break;
      case "favorites":
        boardList = questionService.listByFavorites(pageNo, pageSize);
        break;
      case "views":
        boardList = questionService.listByViews(pageNo, pageSize);
        break;
      default: //"latest"
        boardList = questionService.list(pageNo, pageSize);
        break;
    }

    if (pageNo < 1) {
      pageNo = 1;
    }

    int length = questionService.countAll();

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
    model.addAttribute("sort", sort);

    return "question/list";
  }

  @GetMapping("view")
  public void view(int boardNo, Model model) throws Exception {

    Board board = questionService.get(boardNo);
    if (board == null) {
      throw new Exception("게시글이 존재하지 않습니다.");
    }
    System.out.println("===================="+board.toString());

    List<Comment> commentList = commentService.list(boardNo);

    questionService.increaseBoardCount(board.getBoardNo());

    model.addAttribute("commentList", commentList);

    model.addAttribute("board", board);

  }

  @PostMapping("view2")
  public void view2(int boardNo, Model model) throws Exception {

    Board board = questionService.get(boardNo);


    model.addAttribute("board", board);

  }

  @PostMapping("update")
  public String update(int boardNo, String boardTitle, String boardContent, String boardTag) throws Exception {

    Board board = questionService.get(boardNo);

    board.setBoardTitle(boardTitle);
    board.setBoardContent(boardContent);
    board.setBoardTag(boardTag);

    questionService.update(board);
    return "redirect:view?boardNo=" + boardNo;
  }

  @GetMapping("delete")
  public String delete(int boardNo) throws Exception {

    questionService.delete(boardNo);
    return "redirect:list";
  }

}
