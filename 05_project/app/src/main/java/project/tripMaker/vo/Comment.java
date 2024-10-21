package project.tripMaker.vo;

import java.sql.Date;
import lombok.Data;

@Data
public class Comment {
    private int commentNo;             // 댓글번호
    private int boardNo;               // 게시판번호
    private int userNo;                // 유저번호
    private String commentContent;     // 댓글내용
    private Date commentCreatedDate;   // 댓글작성시간
}
