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
import project.tripMaker.service.BoardService;
import project.tripMaker.service.CommentService;
import project.tripMaker.service.NotificationService;
import project.tripMaker.service.QuestionService;
import project.tripMaker.service.ReviewService;
import project.tripMaker.service.UserService;
import project.tripMaker.vo.Comment;
import project.tripMaker.vo.Notification;
import project.tripMaker.vo.User;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;
  private final NotificationService notificationService;
  private final UserService userService;
  private final ReviewService reviewService;
  private final QuestionService questionService;

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

    if (comment.getCommentContent() == null || comment.getCommentContent().trim().isEmpty()) {
      // throw new IllegalArgumentException("댓글 내용을 입력해주세요.");
    }

    if (boardTypeCode < 1 || boardTypeCode > 3) {
      throw new IllegalArgumentException("유효하지 않은 boardType 값입니다.");
    }

    // boardTypeCode를 BoardType Enum으로 변환
    BoardType boardType = BoardType.fromTypeCode(boardTypeCode);

    comment.setUserNo(loginUser.getUserNo());
    commentService.add(comment);

    // 게시글 작성자 정보 가져오기 - boardType에 따라 분기
    int boardNo = comment.getBoardNo();
    User boardOwner = null;

    switch (boardType) {
      case REVIEW:
        boardOwner = reviewService.get(boardNo).getWriter();
        break;
      case QUESTION:
        boardOwner = questionService.get(boardNo).getWriter();
        break;
      // 필요한 경우 다른 게시판 타입도 추가
    }

    // 알림 생성
    if (boardOwner != null && !boardOwner.getUserNo().equals(loginUser.getUserNo())) {
      String notificationMessage = "회원님이 작성한 게시글에 댓글이 달렸습니다.";
      String notificationLink = "/" + boardType.name().toLowerCase() + "/view?boardNo=" + boardNo;
      notificationService.createNotification(boardOwner.getUserNo(), notificationMessage, notificationLink);
    }

    return "redirect:../" + boardType.name().toLowerCase() + "/view?boardNo=" + comment.getBoardNo();
  }
//   @PostMapping("add")
//   public String add(
//           Comment comment,
//           @RequestParam("boardType") Integer boardTypeCode,
//           HttpSession session) throws Exception {
//
//     User loginUser = (User) session.getAttribute("loginUser");
//     if (loginUser == null) {
//       throw new Exception("로그인이 필요합니다.");
//     }
//
//     if (comment.getCommentContent() == null || comment.getCommentContent().trim().isEmpty()) {
// //       throw new IllegalArgumentException("댓글 내용을 입력해주세요.");
//     }
//
//     if (boardTypeCode < 1 || boardTypeCode > 3) {
//       throw new IllegalArgumentException("유효하지 않은 boardType 값입니다.");
//     }
//
//     // boardTypeCode를 BoardType Enum으로 변환
//     BoardType boardType = BoardType.fromTypeCode(boardTypeCode);
//
//     comment.setUserNo(loginUser.getUserNo());
//     commentService.add(comment);
//
//     // 게시글 작성자에게 알림 전송
//     int boardNo = comment.getBoardNo();
//     User boardOwner = reviewService.get(boardNo).getWriter(); // 게시글 작성자 정보 가져오기
//     if (boardOwner != null && !boardOwner.getUserNo().equals(loginUser.getUserNo())) {
//       String notificationMessage = "회원님이 작성한 게시글에 댓글이 달렸습니다.";
//       String notificationLink = "/" + boardType.name().toLowerCase() + "/view?boardNo=" + boardNo;
//       notificationService.createNotification(boardOwner.getUserNo(), notificationMessage, notificationLink);
//     }
//
//
//     return "redirect:../" + boardType.name().toLowerCase() + "/view?boardNo=" + comment.getBoardNo();
//   }

  @GetMapping("delete")
  public String delete(
          @RequestParam int commentNo,
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


    Comment comment = commentService.get(commentNo);
    if (comment == null) {
      System.out.println("삭제하려는 댓글이 존재하지 않습니다. commentNo: " + commentNo);
      throw new Exception("존재하지 않는 댓글입니다.");
    }

    // 댓글 작성자와 로그인 유저가 같은지 확인
    if (!comment.getUserNo().equals(loginUser.getUserNo()) && !loginUser.getUserRole().name().equals("ROLE_ADMIN")) {
      throw new Exception("삭제 권한이 없습니다.");
    }

    commentService.delete(commentNo);
    return "redirect:../" + boardType.name().toLowerCase() + "/view?boardNo=" + comment.getBoardNo();
  }

//  @PostMapping("/update")
//  @ResponseBody
//  public Map<String, Object> update(
//      // @RequestParam("boardType") int boardTypeCode, @RequestParam("commentNo") Integer commentNo, @RequestParam("content") String content
//      @RequestBody Map<String, Object> request
//  ) {
//
//    Map<String, Object> response = new HashMap<>();
//
//    try {
//      Integer commentNo = (Integer) request.get("commentNo");
//      String content = (String) request.get("content");
//
//      // 댓글 가져오기
//      Comment comment = commentService.get(commentNo);
//      if (comment == null) {
//        throw new Exception("존재하지 않는 댓글입니다.");
//      }
//
//      // 댓글 내용 업데이트
//      comment.setCommentContent(content);
//      commentService.update(comment);
//
//      response.put("success", true);
//    } catch (Exception e) {
//      response.put("success", false);
//      response.put("message", e.getMessage());
//    }
//    return response;
//  }

  @PostMapping("update")
  public String update(
          @RequestParam("boardType") int boardTypeCode,
          @RequestParam("commentNo") Integer commentNo,
          @RequestParam("commentContent") String commentContent,
          @RequestParam("boardNo") Integer boardNo,
          HttpSession session) throws Exception {

    User loginUser = (User) session.getAttribute("loginUser");
    if (loginUser == null) {
      throw new Exception("로그인이 필요합니다.");
    }

//    System.out.println("로그인 사용자: " + loginUser.getUserNo());

    Comment comment = commentService.get(commentNo);
    if (comment == null) {
      throw new Exception("존재하지 않는 댓글입니다.");
    }

    // 댓글 작성자와 로그인 유저가 같은지 확인
    if (!comment.getUserNo().equals(loginUser.getUserNo())) {
      throw new Exception("수정 권한이 없습니다.");
    }

//    System.out.println("업데이트할 댓글 번호: " + commentNo + ", 내용: " + commentContent);

    comment.setCommentContent(commentContent);

    boolean isUpdated = commentService.update(comment);
//    if (!isUpdated) {
//      System.out.println("댓글 업데이트 실패");
//    } else {
//      System.out.println("댓글 업데이트 성공");
//    }

    // boardTypeCode를 BoardType Enum으로 변환
    BoardType boardType = BoardType.fromTypeCode(boardTypeCode);

    return "redirect:../" + boardType.name().toLowerCase() + "/view?boardNo=" + comment.getBoardNo();
  }

  @PostMapping("/toggleCommentLike")
  @ResponseBody
  public Map<String, Object> toggleCommentLike(@RequestParam("commentNo") int commentNo, HttpSession session) throws Exception {
    Map<String, Object> response = new HashMap<>();
    User loginUser = (User) session.getAttribute("loginUser");

    if (loginUser == null) {
      response.put("message", "로그인이 필요합니다.");
      response.put("isCommentLiked", false);
      response.put("commentLikeCount", commentService.getCommentLikeCount(commentNo));
      return response;
    }

    try {
      Map<String, Object> serviceResponse = commentService.toggleCommentLike(commentNo, Math.toIntExact(loginUser.getUserNo()));
      response.putAll(serviceResponse);
    } catch (Exception e) {
      response.put("message", "좋아요 처리 중 오류가 발생했습니다.");
      response.put("isCommentLiked", false);
      response.put("commentLikeCount", commentService.getCommentLikeCount(commentNo));  // 현재 좋아요 수 반환
    }

    return response;
  }

}
