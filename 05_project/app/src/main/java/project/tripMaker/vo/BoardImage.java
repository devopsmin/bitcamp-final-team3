package project.tripMaker.vo;

import lombok.Data;

@Data
public class BoardImage {
    private int boardimageNo;               // 이미지 번호
    private int boardNo;                    // 게시판 번호
    private String boardimageName;          // 이미지 이름 (UUID)
    private String boardimageDefaultName;   // 이미지 이름 (원본)
}
