package project.tripMaker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
public class MyPageController {
    @GetMapping("userpage")
    public String myPage() {
        return "mypage/userpage"; // Thymeleaf 템플릿 경로
    }

}
