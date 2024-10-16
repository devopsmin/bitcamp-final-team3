package project.tripMaker.vo;

import java.sql.Date;

public class Comment {
    private int commentNo;             // 댓글번호
    private int boardNo;               // 게시판번호
    private int userNo;                // 유저번호
    private String commentContent;     // 댓글내용
    private Date commentCreatedDate;   // 댓글작성시간

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

    @Override
    public String toString() {
        return "Comment{" +
            "commentNo=" + commentNo +
            ", boardNo=" + boardNo +
            ", userNo=" + userNo +
            ", commentContent='" + commentContent + '\'' +
            ", commentCreatedDate=" + commentCreatedDate +
            '}';
    }
}
