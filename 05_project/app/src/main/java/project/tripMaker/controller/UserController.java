package project.tripMaker.controller;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.tripMaker.config.PasswordEncoderConfig;
import project.tripMaker.service.StorageService;
import project.tripMaker.service.UserService;
import project.tripMaker.vo.User;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final StorageService storageService;
    private final PasswordEncoderConfig passwordEncoderConfig;

    private final String folderName = "user/profile/";

    @GetMapping("/profile")
    public String myInfo(
            HttpSession session,
            Model model) throws Exception {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            throw new Exception("로그인이 필요합니다.");
        }
        User user = userService.get(loginUser.getUserNo());
        model.addAttribute("user", user);
        return "user/profile/view";
    }

    @GetMapping("/profile/{userNo}")
    public String view(
            @PathVariable Long userNo,
            Model model) throws Exception {
        User user = userService.get(userNo);
        model.addAttribute("user", user);
        return "user/profile/view";
    }

    @PostMapping("/profile/{userNo}")
    public String update(
            @PathVariable Long userNo,
            User user,
            MultipartFile file) throws Exception {

        user.setUserNo(userNo);
        User old = userService.get(userNo);

        if (file != null && file.getSize() > 0) {
            storageService.delete(folderName + old.getUserPhoto());

            String filename = UUID.randomUUID().toString();
            HashMap<String, Object> options = new HashMap<>();
            options.put(StorageService.CONTENT_TYPE, file.getContentType());
            storageService.upload(folderName + filename,
                    file.getInputStream(),
                    options);

            user.setUserPhoto(filename);
        } else {
            user.setUserPhoto(old.getUserPhoto());
        }

        if (userService.update(user)) {
//      return "redirect:/user"; // 기존 원본
            return "redirect:/admin"; // 임의로 경로지정 해놓은거 나중에 바꿔야함 현재는 이렇게 안하면 에러 발생
        } else {
            throw new Exception("없는 회원입니다!");
        }
    }

  @PostMapping("/profile/delete")
  @ResponseBody
  public String deleteAccount(HttpSession session) {
    try {
      User loginUser = (User) session.getAttribute("loginUser");

      if (loginUser == null) {
        return "로그인이 필요합니다.";
      }

      // 사용자 정보 익명화
      loginUser.setUserPhoto(null);
      loginUser.setUserEmail("알수없음_" + System.currentTimeMillis());
      loginUser.setUserNickname("알수없음" + System.currentTimeMillis());
      loginUser.setUserPassword(null);
      loginUser.setUserTel("탈퇴한 사용자");
      loginUser.setSnsNo(null);

      boolean isDeleted = userService.realDelete(loginUser.getUserNo(), loginUser);

      if (isDeleted) {
        // 세션 무효화
        session.invalidate();
        return "회원 탈퇴가 완료되었습니다.";
      } else {
        return "회원 탈퇴 처리 중 오류가 발생했습니다.";
      }
    } catch (Exception e) {
      return "회원 탈퇴 처리 중 오류가 발생했습니다.";
    }
  }



  @PostMapping("/change-password")
  @ResponseBody
  public String changePassword(
      @RequestParam String currentPassword,
      @RequestParam String newPassword,
      HttpSession session) {
    try {
      User loginUser = (User) session.getAttribute("loginUser");
      if (loginUser == null) {
        return "로그인이 필요합니다.";
      }

      User currentUser = userService.get(loginUser.getUserNo());

      if (!passwordEncoderConfig.passwordEncoder().matches(currentPassword, currentUser.getUserPassword())) {
        return "현재 비밀번호가 일치하지 않습니다.";
      }

      currentUser.setUserPassword(passwordEncoderConfig.passwordEncoder().encode(newPassword));
      userService.update(currentUser);

      return "비밀번호가 성공적으로 변경되었습니다.";
    } catch (Exception e) {
      e.printStackTrace();
      return "비밀번호 변경 중 오류가 발생했습니다.";
    }
  }
}
