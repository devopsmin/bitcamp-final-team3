package project.tripMaker.vo;

import lombok.Data;

@Data
public class Companionapply {

    private int companionapplyNo;       // 동행신청 번호
    private Integer companionrecruitNo; // 동행모집 번호
    private Long userNo;                // 동행신청한 유저의 고유번호 저장

}
