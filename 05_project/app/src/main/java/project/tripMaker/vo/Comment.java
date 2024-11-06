package project.tripMaker.vo;

import lombok.Data;
import java.util.Date;

@Data
public class Comment {
    private int commentNo;
    private int boardNo; // 게시글 번호와 연관
    private Long userNo; // 댓글 작성자
    private String commentContent; // 댓글 내용
    private Date commentCreatedDate;
    private User writer; // 작성자

}

