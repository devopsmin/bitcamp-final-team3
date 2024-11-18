package project.tripMaker.vo;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.tripMaker.user.UserRole;

@Getter
@Setter
@NoArgsConstructor
public class User {
  private Long userNo;                // 사용자 번호
  private String userPhoto;           // 사용자 사진
  private LocalDateTime userLastestLogin; // 마지막 로그인 시간
  private String userEmail;           // 사용자 이메일
  private String userPassword;        // 사용자 비밀번호
  private String userTel;             // 사용자 전화번호
  private LocalDateTime userCreatedDate;  // 사용자 생성 날짜
  private String userNickname;        // 사용자 닉네임
  private UserRole userRole;          // 사용자 역할
  private Integer userBlock;          // 사용자 차단 상태   현재 (0:이용 가능, 1:일시 정지, 2:영구 정지, 3:탈퇴 회원) 
  private Integer snsNo;              // SNS 번호 (소셜 로그인 시 사용)
  private String snsName;             // SNS 이름/구분 (구글, 네이버 , 카카오)
  private LocalDateTime deletedDate;  // 탈퇴날짜

  @Builder
  public User(Long userNo, String userPhoto, LocalDateTime userLastestLogin,
              String userEmail, String userPassword, String userTel,
              LocalDateTime userCreatedDate, String userNickname,
              UserRole userRole, Integer userBlock, Integer snsNo,
              String snsName, LocalDateTime deletedDate) {
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
    this.snsName = snsName;
    this.deletedDate = deletedDate;
  }
}
