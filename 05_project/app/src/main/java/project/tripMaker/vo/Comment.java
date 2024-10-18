package project.tripMaker.vo;

import java.util.Date;

public class Comment {
    private int commentNo;
    private int boardNo; // 게시글 번호와 연관
    private int userNo; // 댓글 작성자
    private String commentContent; // 댓글 내용
    private Date commentCreatedDate;

    public int getCommentNo() {
        return commentNo;
    }

    public void setCommentNo(int commentNo) {
        this.commentNo = commentNo;
    }

    public int getBoardNo() {
        return boardNo;
    }

    public void setBoardNo(int boardNo) {
        this.boardNo = boardNo;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Date getCommentCreatedDate() {
        return commentCreatedDate;
    }

    public void setCommentCreatedDate(Date commentCreatedDate) {
        this.commentCreatedDate = commentCreatedDate;
    }
}

