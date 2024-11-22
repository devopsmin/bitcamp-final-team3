package project.tripMaker.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import kotlinx.serialization.descriptors.PrimitiveKind.INT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.tripMaker.service.CommentService;
import project.tripMaker.service.QuestionService;
import project.tripMaker.service.ReviewService;
import project.tripMaker.service.ScheduleService;
import project.tripMaker.vo.Board;
import project.tripMaker.vo.Comment;
import project.tripMaker.vo.Schedule;
import project.tripMaker.vo.Trip;
import project.tripMaker.vo.User;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final ScheduleService scheduleService;
    private final ReviewService reviewService;
    private final QuestionService questionService;
    private final CommentService commentService;

    @GetMapping("preparing")
    public String preparing(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            session.setAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/auth/login";
        }

        if (loginUser.getUserRole().name().equals("ROLE_ADMIN")){
            return "redirect:/admin";
        }


        List<Trip> allTrips = scheduleService.getTripsByUserNo(loginUser.getUserNo());
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysLater = today.plusDays(30);

        List<Trip> preparingTrips = allTrips.stream()
            .filter(trip -> {
                LocalDate startDate = trip.getStartDate().toLocalDate();
                return startDate.isAfter(thirtyDaysLater);
            })
            .sorted((trip1, trip2) -> {
                long day1 = ChronoUnit.DAYS.between(today, trip1.getStartDate().toLocalDate());
                long day2 = ChronoUnit.DAYS.between(today, trip2.getStartDate().toLocalDate());
                return Long.compare(Math.abs(day1), Math.abs(day2));
            })
            .collect(Collectors.toList());

        model.addAttribute("trips", preparingTrips);
        return "mypage/preparing";
    }

    @GetMapping("upcoming")
    public String upcoming(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            session.setAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/auth/login";
        }

        List<Trip> allTrips = scheduleService.getTripsByUserNo(loginUser.getUserNo());
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysLater = today.plusDays(30);

        List<Trip> upcomingTrips = allTrips.stream()
            .filter(trip -> {
                LocalDate startDate = trip.getStartDate().toLocalDate();
                return !startDate.isBefore(today) && !startDate.isAfter(thirtyDaysLater);
            })
            .sorted((trip1, trip2) -> {
                long day1 = ChronoUnit.DAYS.between(today, trip1.getStartDate().toLocalDate());
                long day2 = ChronoUnit.DAYS.between(today, trip2.getStartDate().toLocalDate());
                return Long.compare(Math.abs(day1), Math.abs(day2));
            })
            .collect(Collectors.toList());

        model.addAttribute("trips", upcomingTrips);
        return "mypage/upcoming";
    }

    @GetMapping("completed")
    public String completed(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            session.setAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/auth/login";
        }

        List<Trip> allTrips = scheduleService.getTripsByUserNo(loginUser.getUserNo());
        LocalDate today = LocalDate.now();

        List<Trip> completedTrips = allTrips.stream()
            .filter(trip -> {
                LocalDate endDate = trip.getEndDate().toLocalDate();
                return endDate.isBefore(today);
            })
            .sorted((trip1, trip2) -> {
                long day1 = ChronoUnit.DAYS.between(trip1.getEndDate().toLocalDate(), today);
                long day2 = ChronoUnit.DAYS.between(trip2.getEndDate().toLocalDate(), today);
                return Long.compare(day1, day2);
            })
            .collect(Collectors.toList());

        model.addAttribute("trips", completedTrips);
        return "mypage/completed";
    }

    @GetMapping("boardpage")
    public String boardpage(HttpSession session,
        @RequestParam(defaultValue = "1") int pageNo,
        @RequestParam(defaultValue = "9") int pageSize,
        Model model) throws Exception {
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            session.setAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/auth/login";
        }

        List<Board> reviewBoardList = reviewService.listUser(loginUser.getUserNo());
        List<Board> questionBoardList = questionService.listUser(loginUser.getUserNo());

        // 전체 갯수 확인
        // 페이지 처리
        int length = reviewService.countAll(3);
        int pageCount = length / pageSize;

        // 페이지 처리 최소 1 이상
        if (pageNo < 1) {
            pageNo = 1;
        }

        // 갯수가 size 초과시 페이지 1칸더
        if (length % pageSize > 0) {
            pageCount++;
        }

        // 페이지 처리 최대 PageCount 까지
        if (pageNo > pageCount) {
            pageNo = pageCount;
        }

        // 리뷰 게시물 목록
        for (Board board : reviewBoardList) {
            // 직접 첫번째 이미지 Board객체 추가
            if (board.getBoardImages() != null && !board.getBoardImages().isEmpty()) {
                board.setFirstImageName(board.getBoardImages().get(0).getBoardimageName());
            } else {
                board.setFirstImageName("default.png");
            }

            // 댓글 갯수
            int commentCount = reviewService.getCommentCount(board.getBoardNo());
            board.setCommentCount(commentCount); // 댓글 개수 설정
        }

        // 리뷰 게시물 목록
        for (Board board : questionBoardList) {
            // 댓글 갯수
            int commentCount = reviewService.getCommentCount(board.getBoardNo());
            board.setCommentCount(commentCount); // 댓글 개수 설정
        }


        model.addAttribute("questionList", questionBoardList);
        model.addAttribute("reviewList", reviewBoardList);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pageCount", pageCount);

        return "mypage/boardpage";
    }

    @GetMapping("commentpage")
    public String commentpage(HttpSession session,
        @RequestParam(defaultValue = "1") int pageNo,
        @RequestParam(defaultValue = "10") int pageSize,
        @RequestParam(required = false) String searchType,
        @RequestParam(required = false) String searchText,
        Model model) throws Exception {
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            session.setAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/auth/login";
        }

        if (pageNo < 1) {
            pageNo = 1;
        }


        HashMap<String, Object> options = new HashMap<>();
        options.put("userNo", loginUser.getUserNo());
        options.put("rowNo", (pageNo - 1) * pageSize);
        options.put("length", pageSize);

        // 검색 조건은 선택적으로 추가
        if (searchType != null && searchText != null && !searchText.trim().isEmpty()) {
            options.put("searchType", searchType);
            options.put("searchText", searchText);
        }

        int length = commentService.countAllUserComment(options);

        if (length < 0) {
            length = 0; // 예외 처리
        }

        int pageCount = length / pageSize;
        if (length % pageSize > 0) {
            pageCount++;
        }

        if (pageNo > pageCount) {
            pageNo = pageCount;
        }

        List<Comment> commentList = commentService.listUser(options);

        model.addAttribute("commentList", commentList);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchText", searchText);

        return "mypage/commentpage";
    }

}
