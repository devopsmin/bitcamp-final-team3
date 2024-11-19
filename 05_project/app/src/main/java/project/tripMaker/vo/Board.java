package project.tripMaker.vo;

import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class Board {

  private int boardNo;           // 게시판번호
  private Integer boardtypeNo;   // 게시글분류번호
  private String boardTitle;     // 제목
  private Long userNo;           // 유저번호
  private int boardCount;        // 조회수
  private Date boardCreatedDate; // 작성일
  private Integer tripNo;        // 여행번호
  private String boardContent;   // 내용
  private String boardTag;       // 태그

  private User writer;            // 작성자
  private List<Comment> comments; // 댓글
  private int commentCount;       // 댓글 수

  private int boardLike;          // 좋아요
  private int boardFavor;         // 즐겨찾기
  private int boardCommentCount;  // 댓글 수
  private int tmpNo;              // 순서대로 번호

  private Trip trip;              // 여행
  private List<BoardImage> boardImages;  // 게시판 첨부 이미지

  private String firstImageName;  // 게시글 첫 이미지 썸네일 사진
}
