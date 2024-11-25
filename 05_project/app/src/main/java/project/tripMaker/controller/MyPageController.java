package project.tripMaker.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
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
import project.tripMaker.vo.*;

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

    @GetMapping("questionpage")
    public String questionpage(HttpSession session,
        @RequestParam(defaultValue = "1") int pageNo,
        @RequestParam(defaultValue = "9") int pageSize,
        Model model) throws Exception {
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            session.setAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/auth/login";
        }

        List<Board> questionBoardList = questionService.listUser(loginUser.getUserNo());

        // 전체 갯수 확인
        // 페이지 처리
        int length = questionService.countAll(1);
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


        model.addAttribute("questionList", questionBoardList);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pageCount", pageCount);

        return "mypage/questionpage";
    }

    @GetMapping("reviewpage")
    public String reviewpage(HttpSession session,
        @RequestParam(defaultValue = "1") int pageNo,
        @RequestParam(defaultValue = "9") int pageSize,
        Model model) throws Exception {
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            session.setAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/auth/login";
        }

        List<Board> reviewBoardList = reviewService.listUser(loginUser.getUserNo());

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

        model.addAttribute("reviewList", reviewBoardList);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("pageCount", pageCount);

        return "mypage/reviewpage";
    }


    // 리스트 페이징 처리 메서드
    private List<Board> paginateList(List<Board> list, int pageNo, int pageSize) {
        int start = (pageNo - 1) * pageSize;
        int end = Math.min(start + pageSize, list.size());
        return list.subList(start, end);
    }

    // 페이지 수 계산 메서드
    private int calculatePageCount(int totalSize, int pageSize) {
        return (totalSize + pageSize - 1) / pageSize; // 올림 계산
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


    @GetMapping("view")
    public void view(@RequestParam("tripNo") int tripNo,
                     Model model,
                     HttpSession session) throws Exception {

        User loginUser = (User) session.getAttribute("loginUser");
        boolean isLoggedIn = loginUser != null;

        if (!isLoggedIn) {
            throw new Exception("로그인이 필요합니다.");
        }

        List<Trip> trips = scheduleService.getTripsByUserNo(loginUser.getUserNo());

        if (trips == null || trips.isEmpty()) {
            throw new Exception("사용자와 관련된 Trip 데이터가 없습니다.");
        }

        Trip trip = trips.stream()
                .filter(t -> t.getTripNo() == tripNo)
                .findFirst()
                .orElseThrow(() -> new Exception("해당 Trip을 찾을 수 없습니다."));

        if (trip.getStartDate() == null || trip.getEndDate() == null) {
            throw new Exception("Trip의 날짜 데이터가 유효하지 않습니다.");
        }

        List<Schedule> scheduleList = scheduleService.getSchedulesByTripNo(tripNo);

        LocalDate startDate = trip.getStartDate().toLocalDate();

        Map<Integer, List<Schedule>> groupedSchedules = scheduleList.stream()
                .collect(Collectors.groupingBy(Schedule::getScheduleDay));

        Map<Integer, String> dayDates = groupedSchedules.keySet().stream()
                .collect(Collectors.toMap(
                        day -> day,
                        day -> startDate.plusDays(day - 1).format(DateTimeFormatter.ofPattern("yyyy.MM.dd(E)"))
                ));

        if (groupedSchedules.isEmpty()) {
            model.addAttribute("noSchedulesMessage", "등록된 일정이 없습니다.");
        }

        boolean isUserAuthorized = Objects.equals(loginUser.getUserNo(), trip.getUserNo()) ||
                "ROLE_ADMIN".equals(loginUser.getUserRole().name());

        model.addAttribute("trip", trip);
        model.addAttribute("groupedSchedules", groupedSchedules);
        model.addAttribute("dayDates", dayDates);
        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("isUserAuthorized", isUserAuthorized);
    }

}
