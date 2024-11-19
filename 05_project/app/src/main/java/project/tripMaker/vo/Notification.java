package project.tripMaker.vo;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Notification {
    private Long notificationNo;        // 알림 번호
    private Long userNo;                // 유저 번호
    private String notiMessage;         // 알림 메세지
    private String notiLink;            // URL
    private boolean isRead;             // 읽음 여부
    private LocalDateTime createdDate;  // 생성일
}
