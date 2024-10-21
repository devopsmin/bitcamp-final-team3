package project.tripMaker.vo;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  private int userNo;                         // 유저번호
  private String userPhoto;                   // 프로필사진
  private java.sql.Date userLastestLogin;     // 마지막로그인날짜
  private String userEmail;                   // 이메일
  private String userPassword;                // 비밀번호
  private String userTel;                     // 연락처
  private Date userCreatedDate;               // 가입일
  private String userNickname;                // 닉네임
  private boolean userAutherlize;             // 권한여부 (TINYINT -> boolean)
  private boolean userBlock;                  // 차단여부 (TINYINT -> boolean)
  private Integer snsNo;                      // SNS Login 유형번호 (nullable, hence Integer)

  public User() {
  }

  public User(int userNo) {
    this.userNo = userNo;
  }

  public int getUserNo() {
    return userNo;
  }

  public void setUserNo(int userNo) {
    this.userNo = userNo;
  }

  public String getUserPhoto() {
    return userPhoto;
  }

  public void setUserPhoto(String userPhoto) {
    this.userPhoto = userPhoto;
  }

  public Date getUserLastestLogin() {
    return userLastestLogin;
  }

  public void setUserLastestLogin(Date userLastestLogin) {
    this.userLastestLogin = userLastestLogin;
  }

  public String getUserEmail() {
    return userEmail;
  }

  public void setUserEmail(String userEmail) {
    this.userEmail = userEmail;
  }

  public String getUserPassword() {
    return userPassword;
  }

  public void setUserPassword(String userPassword) {
    this.userPassword = userPassword;
  }

  public String getUserTel() {
    return userTel;
  }

  public void setUserTel(String userTel) {
    this.userTel = userTel;
  }

  public Date getUserCreatedDate() {
    return userCreatedDate;
  }

  public void setUserCreatedDate(Date userCreatedDate) {
    this.userCreatedDate = userCreatedDate;
  }

  public String getUserNickname() {
    return userNickname;
  }

  public void setUserNickname(String userNickname) {
    this.userNickname = userNickname;
  }

  public boolean isUserAutherlize() {
    return userAutherlize;
  }

  public void setUserAutherlize(boolean userAutherlize) {
    this.userAutherlize = userAutherlize;
  }

  public boolean isUserBlock() {
    return userBlock;
  }

  public void setUserBlock(boolean userBlock) {
    this.userBlock = userBlock;
  }

  public Integer getSnsNo() {
    return snsNo;
  }

  public void setSnsNo(Integer snsNo) {
    this.snsNo = snsNo;
  }

  @Override
  public String toString() {
    return "User{" +
            "userNo=" + userNo +
            ", userPhoto='" + userPhoto + '\'' +
            ", userLastestLogin=" + userLastestLogin +
            ", userEmail='" + userEmail + '\'' +
            ", userPassword='" + userPassword + '\'' +
            ", userTel='" + userTel + '\'' +
            ", userCreatedDate=" + userCreatedDate +
            ", userNickname='" + userNickname + '\'' +
            ", userAutherlize=" + userAutherlize +
            ", userBlock=" + userBlock +
            ", snsNo=" + snsNo +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return userNo == user.userNo;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(userNo);
  }
}
