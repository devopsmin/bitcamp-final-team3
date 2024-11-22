package project.tripMaker.vo;

import java.util.Date;
import lombok.Data;

@Data
public class Comment {
    private int commentNo;
    private int boardNo; // 게시글 번호와 연관
    private Long userNo; // 댓글 작성자
    private String commentContent; // 댓글 내용
    private Date commentCreatedDate;
    private User writer; // 작성자

    private int commentLike;  // 댓글 좋아요

    private int boardtypeNo; // 게시글 타입
    private String boardTitle; // 게시판 제목
}

