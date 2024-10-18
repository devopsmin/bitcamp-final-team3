package project.tripMaker.vo;

import org.joda.time.DateTime;

import java.util.Date;

public class User {

    private int userNo;            // 유저번호
    private String userPhoto;      // 프로필 사진
    private DateTime userLastestLogin; // 마지막 로그인 날짜
    private String userEmail;      // 이메일
    private String userPassword;   // 비밀번호
    private String userTel;        // 연락처
    private Date userCreatedDate;      // 가입일
    private String userNickname;   // 닉네임
    private boolean userAuthorize; // 권한여부 (TINYINT, true/false로 변환)
    private boolean userBlock;     // 차단여부 (TINYINT, true/false로 변환)
    private Integer snsNo;

    public User() {
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

    public DateTime getUserLastestLogin() {
        return userLastestLogin;
    }

    public void setUserLastestLogin(DateTime userLastestLogin) {
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

    public Date getCreatedDate() {
        return userCreatedDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.userCreatedDate = createdDate;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public boolean isUserAuthorize() {
        return userAuthorize;
    }

    public void setUserAuthorize(boolean userAuthorize) {
        this.userAuthorize = userAuthorize;
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

    // toString 메서드 (디버깅용)
    @Override
    public String toString() {
        return "User{" +
                "userNo=" + userNo +
                ", userPhoto='" + userPhoto + '\'' +
                ", lastestLogin=" + userLastestLogin +
                ", userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userTel='" + userTel + '\'' +
                ", createdDate=" + userCreatedDate +
                ", userNickname='" + userNickname + '\'' +
                ", userAuthorize=" + userAuthorize +
                ", userBlock=" + userBlock +
                ", snsNo=" + snsNo +
                '}';
    }
}
