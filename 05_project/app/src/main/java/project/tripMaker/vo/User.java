package project.tripMaker.vo;

import java.sql.Date;
import lombok.Data;

@Data
public class User {
    private int userNo;                // 유저번호
    private String userPhoto;          // 프로필사진
    private Date userLastestLogin;     // 마지막로그인날짜
    private String userEmail;          // 이메일
    private String userPassword;       // 비밀번호
    private String userTel;            // 연락처
    private Date userCreatedDate;      // 가입일
    private String userNickname;       // 닉네임
    private boolean userAutherlize;    // 권한여부 (TINYINT -> boolean)
    private boolean userBlock;         // 차단여부 (TINYINT -> boolean)
    private Integer snsNo;             // SNS번호 (nullable, hence Integer)
}
