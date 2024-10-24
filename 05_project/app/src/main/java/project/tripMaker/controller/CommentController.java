package project.tripMaker.controller;

import java.util.List;
import javax.servlet.http.HttpSession;
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
import project.tripMaker.vo.User;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  @GetMapping("list/{boardNo}")
  public String list(
      @PathVariable int boardNo,
      Model model) throws Exception {

    List<Comment> commentList = commentService.list(boardNo);
    System.out.println("댓글 목록 크기: " + commentList.size());
    model.addAttribute("commentList", commentList);

    return "board/view?boardNo=" + boardNo;
  }

  @PostMapping("add")
  public String add(
      Comment comment,
      HttpSession session) throws Exception {

    User loginUser = (User) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new Exception("로그인이 필요합니다.");
    }

    comment.setUserNo(loginUser.getUserNo());
    commentService.add(comment);

    return "redirect:/board/view?boardNo=" + comment.getBoardNo();
  }

  @GetMapping("delete")
  public String delete(
      @RequestParam int commentNo,
      @RequestParam int boardNo,
      HttpSession session) throws Exception {

    User loginUser = (User) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new Exception("로그인이 필요합니다.");
    }

    Comment comment = commentService.get(commentNo);
    if (comment == null) {
      throw new Exception("존재하지 않는 댓글입니다.");
    }

    // 댓글 작성자와 로그인 유저가 같은지 확인
    if (!comment.getUserNo().equals(loginUser.getUserNo())) {
      throw new Exception("삭제 권한이 없습니다.");
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