package project.tripMaker.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import project.tripMaker.board.BoardType;
import project.tripMaker.service.CommentService;
import project.tripMaker.service.CommentService;
import project.tripMaker.service.UserService;
import project.tripMaker.vo.Comment;
import project.tripMaker.vo.User;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;
  private final UserService userService;

  @GetMapping("list/{boardNo}")
  public String list(
      @PathVariable int boardNo,
      Model model) throws Exception {

    List<Comment> commentList = commentService.list(boardNo);
    System.out.println("댓글 목록 크기: " + commentList.size());
    model.addAttribute("commentList", commentList);

    return "view?boardNo=" + boardNo;
  }

  @PostMapping("add")
  public String add(
      Comment comment,
      @RequestParam("boardType") Integer boardTypeCode,
      HttpSession session) throws Exception {

    User loginUser = (User) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new Exception("로그인이 필요합니다.");
    }

    if (boardTypeCode < 1 || boardTypeCode > 3) {
      throw new IllegalArgumentException("유효하지 않은 boardType 값입니다.");
    }

    // boardTypeCode를 BoardType Enum으로 변환
    BoardType boardType = BoardType.fromTypeCode(boardTypeCode);

    comment.setUserNo(loginUser.getUserNo());
    commentService.add(comment);

    return "redirect:../" + boardType.name().toLowerCase() + "/view?boardNo=" + comment.getBoardNo();
  }

  @GetMapping("delete")
  public String delete(
      @RequestParam int commentNo,
      @RequestParam("boardType") int boardTypeCode,
      HttpSession session) throws Exception {

    User loginUser = (User) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new Exception("로그인이 필요합니다.");
    }

    if (boardTypeCode < 1 || boardTypeCode > 3) {
      throw new IllegalArgumentException("유효하지 않은 boardType 값입니다.");
    }

    // boardTypeCode를 BoardType Enum으로 변환
    BoardType boardType = BoardType.fromTypeCode(boardTypeCode);


    Comment comment = commentService.get(commentNo);
    if (comment == null) {
      System.out.println("삭제하려는 댓글이 존재하지 않습니다. commentNo: " + commentNo);
      throw new Exception("존재하지 않는 댓글입니다.");
    }

    // 댓글 작성자와 로그인 유저가 같은지 확인
    if (!comment.getUserNo().equals(loginUser.getUserNo())) {
      throw new Exception("삭제 권한이 없습니다.");
    }

    commentService.delete(commentNo);
    return "redirect:../" + boardType.name().toLowerCase() + "/view?boardNo=" + comment.getBoardNo();
  }

  @PostMapping("/update")
  @ResponseBody
  public Map<String, Object> update(
      // @RequestParam("boardType") int boardTypeCode, @RequestParam("commentNo") Integer commentNo, @RequestParam("content") String content
      @RequestBody Map<String, Object> request
  ) {

    Map<String, Object> response = new HashMap<>();

    try {
      Integer commentNo = (Integer) request.get("commentNo");
      String content = (String) request.get("content");

      // 댓글 가져오기
      Comment comment = commentService.get(commentNo);
      if (comment == null) {
        throw new Exception("존재하지 않는 댓글입니다.");
      }

      // 댓글 내용 업데이트
      comment.setCommentContent(content);
      commentService.update(comment);

      response.put("success", true);
    } catch (Exception e) {
      response.put("success", false);
      response.put("message", e.getMessage());
    }
    return response;
  }
}
