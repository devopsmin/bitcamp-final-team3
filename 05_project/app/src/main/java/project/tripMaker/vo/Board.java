package project.tripMaker.vo;

import java.sql.Date;
import java.util.List;
import lombok.Data;

@Data
public class Board {

  private int boardNo;           // 게시판번호
  private Integer boardtypeNo;    // 게시글분류번호
  private String boardTitle;     // 제목
  private int boardCount;        // 조회수
  private Date boardCreatedDate; // 작성일
  private Integer tripNo;        // 여행번호
  private String boardContent;   // 내용
  private String boardTag;       // 태그

  private User writer; // 작성자
  private List<Comment> comments; // 댓글

  private int boardLike;  // 좋아요
  private int boardFavor; // 즐겨찾기

  private int tmpNo; // 순서대로 번호

}
