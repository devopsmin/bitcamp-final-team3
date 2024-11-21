package project.tripMaker.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.tripMaker.service.ScheduleService;
import project.tripMaker.vo.Schedule;
import project.tripMaker.vo.Trip;
import project.tripMaker.vo.User;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final ScheduleService scheduleService;

    @GetMapping("userpage")
    public String myPage(HttpSession session) {

        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            session.setAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/auth/login";
        }

        if (loginUser.getUserRole().name().equals("ROLE_ADMIN")){
            return "redirect:/admin";
        }

        return "mypage/userpage";
    }

    @GetMapping("preparing")
    public String preparing(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            session.setAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/auth/login";
        }

        List<Trip> allTrips = scheduleService.getTripsByUserNo(loginUser.getUserNo());
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysLater = today.plusDays(30);

        List<Trip> preparingTrips = allTrips.stream()
            .filter(trip -> {
                LocalDate startDate = trip.getStartDate().toLocalDate();
                return startDate.isAfter(thirtyDaysLater);
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
            .collect(Collectors.toList());

        model.addAttribute("trips", completedTrips);
        return "mypage/completed";
    }

}
