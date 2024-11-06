package project.tripMaker.vo;

import lombok.Data;

@Data
public class Companionrecruit {

    private int companionrecruitNo;     // 동행모집 번호
    private int boardNo;                // 게시판번호
    private Integer scheduleNo;         // 여행일정번호
    private Boolean checked;            // 동행모집 선택체크
    private Boolean checkedAll;         // 동행모집 전체 체크
    private Integer recruitMaxNo;       // 모집 최대인원수

}