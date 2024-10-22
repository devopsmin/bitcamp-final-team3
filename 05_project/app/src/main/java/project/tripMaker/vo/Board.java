package project.tripMaker.vo;

import java.util.Date;

public class Board {

  private int boardNo;           // 게시판번호
  private Integer boardtypeNo;    // 게시글분류번호
  private String boardTitle;     // 제목
  private int boardCount;        // 조회수
  private Date boardCreatedDate; // 작성일
  private Integer userNo;        // 유저번호
  private Integer tripNo;        // 여행번호
  private String boardContent;   // 내용
  private String boardTag;       // 태그

  // 기본 생성자
  public Board() {}

  // Getters and Setters
  public int getBoardNo() {
    return boardNo;
  }

  public void setBoardNo(int boardNo) {
    this.boardNo = boardNo;
  }

  public Integer getBoardtypeNo() {
    return boardtypeNo;
  }

  public void setBoardtypeNo(Integer boardtypeNo) {
    this.boardtypeNo = boardtypeNo;
  }

  public String getBoardTitle() {
    return boardTitle;
  }

  public void setBoardTitle(String boardTitle) {
    this.boardTitle = boardTitle;
  }

  public int getBoardCount() {
    return boardCount;
  }

  public void setBoardCount(int boardCount) {
    this.boardCount = boardCount;
  }

  public Date getBoardCreatedDate() {
    return boardCreatedDate;
  }

  public void setBoardCreatedDate(Date boardCreatedDate) {
    this.boardCreatedDate = boardCreatedDate;
  }

  public Integer getUserNo() {
    return userNo;
  }

  public void setUserNo(Integer userNo) {
    this.userNo = userNo;
  }

  public Integer getTripNo() {
    return tripNo;
  }

  public void setTripNo(Integer tripNo) {
    this.tripNo = tripNo;
  }

  public String getBoardContent() {
    return boardContent;
  }

  public void setBoardContent(String boardContent) {
    this.boardContent = boardContent;
  }

  public String getBoardTag() {
    return boardTag;
  }

  public void setBoardTag(String boardTag) {
    this.boardTag = boardTag;
  }

  // toString 메서드 (디버깅용)
  @Override
  public String toString() {
    return "Board{" +
        "boardNo=" + boardNo +
        ", boardtypeNo=" + boardtypeNo +
        ", boardTitle='" + boardTitle + '\'' +
        ", boardCount=" + boardCount +
        ", boardCreatedDate=" + boardCreatedDate +
        ", userNo=" + userNo +
        ", tripNo=" + tripNo +
        ", boardContent='" + boardContent + '\'' +
        ", boardTag='" + boardTag + '\'' +
        '}';
  }
}
