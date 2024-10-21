package project.tripMaker.vo;

import java.sql.Timestamp;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.tripMaker.user.UserRole;

@Getter
@Setter
@NoArgsConstructor
public class User {
  private long userNo;                // 사용자 번호
  private String userPhoto;           // 사용자 사진
  private Timestamp userLastestLogin; // 마지막 로그인 시간
  private String userEmail;           // 사용자 이메일
  private String userPassword;        // 사용자 비밀번호
  private String userTel;             // 사용자 전화번호
  private Timestamp userCreatedDate;  // 사용자 생성 날짜
  private String userNickname;        // 사용자 닉네임
  private UserRole userRole;          // 사용자 역할
  private Integer userBlock;          // 사용자 차단 상태
  private Integer snsNo;              // SNS 번호 (소셜 로그인 시 사용)

  @Builder
  public User(long userNo, String userPhoto, Timestamp userLastestLogin, String userEmail,
      String userPassword, String userTel, Timestamp userCreatedDate, String userNickname,
      UserRole userRole, Integer userBlock, Integer snsNo) {
    this.userNo = userNo;
    this.userPhoto = userPhoto;
    this.userLastestLogin = userLastestLogin;
    this.userEmail = userEmail;
    this.userPassword = userPassword;
    this.userTel = userTel;
    this.userCreatedDate = userCreatedDate;
    this.userNickname = userNickname;
    this.userRole = userRole;
    this.userBlock = userBlock;
    this.snsNo = snsNo;
  }
}
