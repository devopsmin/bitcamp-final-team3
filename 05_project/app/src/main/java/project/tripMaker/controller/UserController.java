package project.tripMaker.controller;

import java.util.HashMap;
import java.util.UUID;
import javax.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.tripMaker.service.StorageService;
import project.tripMaker.service.UserService;
import project.tripMaker.vo.User;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final StorageService storageService;
    private final PasswordEncoder passwordEncoder;

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
      @RequestParam(required = false) String currentPassword,
      User user,
      HttpSession session,
      MultipartFile file) {

    try {
      User loginUser = (User) session.getAttribute("loginUser");

      if (loginUser == null) {
        session.setAttribute("errorMessage", "로그인이 필요합니다.");
        return "redirect:/auth/login";
      }

      User currentUser = userService.get(userNo);
      if (currentUser == null) {
        session.setAttribute("errorMessage", "존재하지 않는 회원입니다.");
        return "redirect:/user/profile";
      }

      if (currentUser.getSnsNo() == null) {
        if (currentPassword == null || currentPassword.trim().isEmpty()) {
          session.setAttribute("errorMessage", "현재 비밀번호를 입력해주세요.");
          return "redirect:/user/profile";
        }

        if (!passwordEncoder.matches(currentPassword, currentUser.getUserPassword())) {
          session.setAttribute("errorMessage", "현재 비밀번호가 일치하지 않습니다.");
          return "redirect:/user/profile";
        }
      }

      User updateUser = new User();
      updateUser.setUserNo(userNo);
      updateUser.setUserEmail(currentUser.getUserEmail());
      updateUser.setUserPassword(currentUser.getUserPassword());
      updateUser.setUserTel(user.getUserTel());
      updateUser.setUserNickname(user.getUserNickname());
      updateUser.setUserRole(currentUser.getUserRole());
      updateUser.setUserBlock(currentUser.getUserBlock());
      updateUser.setSnsNo(currentUser.getSnsNo());

      if (file != null && !file.isEmpty()) {
        if (currentUser.getUserPhoto() != null) {
          storageService.delete(folderName + currentUser.getUserPhoto());
        }
        String filename = UUID.randomUUID().toString();
        HashMap<String, Object> options = new HashMap<>();
        options.put(StorageService.CONTENT_TYPE, file.getContentType());
        storageService.upload(folderName + filename, file.getInputStream(), options);
        updateUser.setUserPhoto(filename);
      } else {
        updateUser.setUserPhoto(currentUser.getUserPhoto());
      }

      boolean updated = userService.update(updateUser);

      if (updated) {
        User updatedUser = userService.get(userNo);
        session.setAttribute("loginUser", updatedUser);
        session.setAttribute("successMessage", "회원정보가 성공적으로 수정되었습니다.");
      } else {
        session.setAttribute("errorMessage", "회원정보 수정에 실패했습니다.");
      }

    } catch (Exception e) {
      session.setAttribute("errorMessage", "오류가 발생했습니다: " + e.getMessage());
    }

    return "redirect:/user/profile";
  }

  @PostMapping("/profile/delete")
  @ResponseBody
  public String deleteAccount(HttpSession session) {
    try {
      User loginUser = (User) session.getAttribute("loginUser");

      if (loginUser == null) {
        return "로그인이 필요합니다.";
      }

      loginUser.setUserPhoto(null);
      loginUser.setUserEmail("탈퇴한 유저_" + System.currentTimeMillis());
      loginUser.setUserNickname("알수없음_" + System.currentTimeMillis());
      loginUser.setUserPassword(null);
      loginUser.setUserTel("탈퇴한 사용자");
      loginUser.setSnsNo(null);

      boolean isDeleted = userService.realDelete(loginUser.getUserNo(), loginUser);
      if (isDeleted) {
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

      if (!passwordEncoder.matches(currentPassword, currentUser.getUserPassword())) {
        return "현재 비밀번호가 일치하지 않습니다.";
      }

      currentUser.setUserPassword(passwordEncoder.encode(newPassword));
      userService.update(currentUser);

      return "비밀번호가 성공적으로 변경되었습니다.";
    } catch (Exception e) {
      e.printStackTrace();
      return "비밀번호 변경 중 오류가 발생했습니다.";
    }
  }

}
