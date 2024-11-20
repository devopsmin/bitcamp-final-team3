package project.tripMaker.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.tripMaker.service.NotificationService;
import project.tripMaker.service.UserService;
import project.tripMaker.vo.Notification;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    // 현재 로그인한 사용자의 userNo 추출
    private Long getLoggedInUserNo() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication object: " + authentication); // 로그 추가
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }

        Long userNo = userService.getUserNoByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return userNo;
    }

    // 읽지 않은 알림 조회
    @GetMapping("unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications() throws Exception {
        Long userNo = getLoggedInUserNo();
        System.out.println("Fetching unread notifications for userNo: " + userNo); // 로그 추가
        List<Notification> notifications = notificationService.getUnreadNotifications(userNo);
        System.out.println("Unread notifications: " + notifications); // 로그 추가
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
    public ResponseEntity<List<Notification>> getAllNotifications() throws Exception {
        Long userNo = getLoggedInUserNo();
        List<Notification> notifications = notificationService.getAllNotifications(userNo);
        return ResponseEntity.ok(notifications);
    }

    // // 새로운 알림 여부 확인
    // @GetMapping("check")
    // public ResponseEntity<Boolean> hasUnreadNotifications() throws Exception {
    //     Long userNo = getLoggedInUserNo();
    //     System.out.println("Checking notifications for userNo: " + userNo); // 로그 추가
    //     boolean hasUnread = !notificationService.getUnreadNotifications(userNo).isEmpty();
    //     System.out.println("Unread notifications exist: " + hasUnread); // 로그 추가
    //     return ResponseEntity.ok(hasUnread);
    // }

    // 새로운 알림 개수 확인
    @GetMapping("check")
    public ResponseEntity<Integer> getUnreadNotificationCount() throws Exception {
        Long userNo = getLoggedInUserNo();
        System.out.println("Checking notifications for userNo: " + userNo); // 로그 추가
        int unreadCount = notificationService.getUnreadNotifications(userNo).size();
        System.out.println("Unread notification count: " + unreadCount); // 로그 추가
        return ResponseEntity.ok(unreadCount);
    }

}