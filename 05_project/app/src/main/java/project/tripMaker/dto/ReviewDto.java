package project.tripMaker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private int boardNo;          // 게시글 번호
    private String boardTitle;      // 게시글 제목
    private String firstImageName;  // 첫 번째 이미지 파일 이름 (대표 이미지)
    private String cityName;        // 도시 이름
    private String stateName;       // 주 이름
    private String themaName;       // 테마 이름
    private int boardLike;          // 좋아요 수
    private int boardFavor;         // 즐겨찾기 수
    private int boardCount;         // 조회수
    private String writerNickname;  // 작성자 닉네임
    private String writerPhoto;     // 작성자 사진
    private LocalDate boardCreatedDate;  // 작성 날짜
    private int commentCount;        // 댓글 갯수
}
