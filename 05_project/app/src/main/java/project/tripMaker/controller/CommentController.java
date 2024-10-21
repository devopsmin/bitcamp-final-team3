package project.tripMaker.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.tripMaker.service.CommentService;
import project.tripMaker.service.CommentService;
import project.tripMaker.vo.Comment;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  @GetMapping("list/{boardNo}")
  public String list(
      @PathVariable int boardNo,
      Model model) throws Exception {

    List<Comment> list = commentService.list(boardNo);
    model.addAttribute("list", list);

    return "redirect:/board/view?boardNo=" + boardNo;
  }

  @PostMapping("add")
  public String add(Comment comment) throws Exception {

    commentService.add(comment);

    return "redirect:/board/view?boardNo=" + comment.getBoardNo();
  }

  @GetMapping("delete")
  public String delete(
      @RequestParam int commentNo,
      @RequestParam int boardNo) throws Exception {

    Comment comment = commentService.get(commentNo);

    if (comment == null) {
      throw new Exception("존재하지 않는 댓글입니다.");
    }

    commentService.delete(commentNo);
    return "redirect:/board/view?boardNo=" + boardNo;
  }

  @PostMapping("update")
  public String update(
      @RequestParam("commentNo") Integer commentNo,
      @RequestParam("content") String content,
      @RequestParam("boardNo") Integer boardNo) throws Exception {

    Comment comment = commentService.get(commentNo);
    if (comment == null) {
      throw new Exception("존재하지 않는 댓글입니다.");
    }

    comment.setCommentContent(content);
    commentService.update(comment);

    return "redirect:/board/view?boardNo=" + boardNo;
  }
}
