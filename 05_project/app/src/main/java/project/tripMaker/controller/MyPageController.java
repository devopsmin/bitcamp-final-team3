package project.tripMaker.controller;

import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.tripMaker.vo.User;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

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

}
