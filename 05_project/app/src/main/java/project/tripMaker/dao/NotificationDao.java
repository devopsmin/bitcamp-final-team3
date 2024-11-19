package project.tripMaker.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.tripMaker.vo.Notification;

@Mapper
public interface NotificationDao {

    // 알림 생성
    void insertNotification(Notification notification);

    // 특정 알림 읽음 처리
    void markNotificationAsRead(@Param("notificationNo") Long notificationNo);

    // 읽지 않은 알림 조회
    List<Notification> findUnreadNotifications(@Param("userNo") Long userNo);

    // 유저의 모든 알림 조회 (선택 사항)
    List<Notification> findAllNotifications(@Param("userNo") Long userNo);
}
