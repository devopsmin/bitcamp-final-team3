package project.tripMaker.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import project.tripMaker.service.BenService;
import project.tripMaker.service.StorageService;
import project.tripMaker.service.UserService;
import project.tripMaker.vo.Ben;
import project.tripMaker.vo.User;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {

  private final UserService userService;
  private final StorageService storageService;
  private final BenService benService;


  private final String folderName = "user/profile/";

  @GetMapping
  public String list(
      @RequestParam(defaultValue = "1") int pageNo,
      @RequestParam(defaultValue = "10") int pageSize,
      @RequestParam(required = false) String searchType,
      @RequestParam(required = false) String searchText,
      Model model) throws Exception {

    if (pageNo < 1) {
      pageNo = 1;
    }

    HashMap<String, Object> options = new HashMap<>();
    options.put("rowNo", (pageNo - 1) * pageSize);
    options.put("length", pageSize);
    options.put("searchType", searchType);
    options.put("searchText", searchText);

    int length = userService.countAll(options);

    int pageCount = length / pageSize;
    if (length % pageSize > 0) {
      pageCount++;
    }

    if (pageNo > pageCount) {
      pageNo = pageCount;
    }

    List<User> list = userService.list(options);
    model.addAttribute("list", list);
    model.addAttribute("pageNo", pageNo);
    model.addAttribute("pageSize", pageSize);
    model.addAttribute("pageCount", pageCount);
    model.addAttribute("searchType", searchType);
    model.addAttribute("searchText", searchText);

    return "admin/list";
  }

  @Transactional
  @DeleteMapping("/{userNo}")
  @ResponseBody
  public String delete(@PathVariable Long userNo) throws Exception {
    User old = userService.get(userNo);
    if (userService.delete(userNo)) {
      storageService.delete(folderName + old.getUserPhoto());
      return "success";
    } else {
      return "failure";
    }
  }

  @PostMapping("/ban")
  @ResponseBody
  public String banUser(
      @RequestParam Long userNo,
      @RequestParam Integer bentypeNo,
      @RequestParam String benDesc,
      @RequestParam Integer duration) {

    try {
      User user = userService.get(userNo);
      if (user == null) {
        return "사용자를 찾을 수 없습니다.";
      }

      Ben ben = new Ben();
      ben.setUserNo(userNo);
      ben.setBentypeNo(bentypeNo);
      ben.setBenDesc(benDesc);
      ben.setBenDate(LocalDateTime.now());

      if (duration == -1) {
        user.setUserBlock(2);  // 영구정지
        ben.setUnbanDate(null);
      } else {
        user.setUserBlock(1);  // 일시정지
        ben.setUnbanDate(LocalDateTime.now().plusDays(duration));
      }

      if (userService.update(user)) {
        benService.add(ben);
        return "success";
      } else {
        return "차단 처리에 실패했습니다.";
      }
    } catch (Exception e) {
      return "차단 처리 중 오류가 발생했습니다." + e.getMessage();
    }
  }

  @PostMapping("/unban")
  @ResponseBody
  public String unbanUser(@RequestParam Long userNo) {
    try {
      User user = userService.get(userNo);
      if (user == null) {
        return "사용자를 찾을 수 없습니다.";
      }

      user.setUserBlock(0);

      if (userService.update(user)) {
        Ben ben = benService.getByUserNo(userNo);
        if (ben != null) {
          ben.setUnbanDate(LocalDateTime.now());
          benService.update(ben);
        }
        return "success";
      } else {
        return "차단 해제 처리에 실패했습니다.";
      }
    } catch (Exception e) {
      e.printStackTrace();
      return "차단 해제 처리 중 오류가 발생했습니다.";
    }
  }

}
