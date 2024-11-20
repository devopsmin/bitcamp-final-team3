package project.tripMaker.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.tripMaker.dao.NotificationDao;
import project.tripMaker.vo.Notification;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationDao notificationDao;

    // 알림 생성
    public void createNotification(Long userNo, String message, String link) {
        Notification notification = new Notification();
        notification.setUserNo(userNo);
        notification.setNotiMessage(message);
        notification.setNotiLink(link);
        notification.setRead(false);
        notification.setCreatedDate(null); // DB에서 NOW()로 설정
        notificationDao.insertNotification(notification);
    }

    // 읽지 않은 알림 조회
    public List<Notification> getUnreadNotifications(Long userNo) {
        System.out.println("Fetching notifications for userNo: " + userNo);
        List<Notification> notifications = notificationDao.findUnreadNotifications(userNo);
        System.out.println("Fetched notifications: " + notifications);
        return notificationDao.findUnreadNotifications(userNo);
    }

    // 특정 알림 읽음 처리
    @Transactional
    public void markAsRead(Long notificationNo) {
        notificationDao.markNotificationAsRead(notificationNo);
    }

    // 유저의 모든 알림 조회
    public List<Notification> getAllNotifications(Long userNo) {
        return notificationDao.findAllNotifications(userNo);
    }
}
