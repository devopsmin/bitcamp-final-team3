package project.tripMaker.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import project.tripMaker.service.NotificationService;
import project.tripMaker.service.UserService;
import project.tripMaker.vo.Notification;
import project.tripMaker.vo.User;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    // 읽지 않은 알림 조회
    @GetMapping("unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(Long userNo) throws Exception {
        userService.get(userNo);
        List<Notification> notifications = notificationService.getUnreadNotifications(userNo);
        return ResponseEntity.ok(notifications);
    }

    // 알림 읽음 처리
    @PostMapping("{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable("id") Long notificationNo) {
        notificationService.markAsRead(notificationNo);
        return ResponseEntity.ok().build();
    }

    // 유저의 모든 알림 조회
    @GetMapping("all")
    public ResponseEntity<List<Notification>> getAllNotifications(@RequestParam Long userNo) {
        List<Notification> notifications = notificationService.getAllNotifications(userNo);
        return ResponseEntity.ok(notifications);
    }

    // 새로운 알림 여부 확인
    @GetMapping("check")
    public ResponseEntity<Boolean> hasUnreadNotifications(@RequestParam Long userNo) {
        boolean hasUnread = !notificationService.getUnreadNotifications(userNo).isEmpty();
        return ResponseEntity.ok(hasUnread);
    }
}
