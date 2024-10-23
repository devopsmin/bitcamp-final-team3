package project.tripMaker.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of ={"locationName","locationDesc"})
public class Location {
  private Integer locationNo;      // 여행지정보 번호
  private String thirdclassCode;    // 소분류번호
  private String cityCode;
  private String locationName;     // 여행지이름
  private String locationDesc;     // 여행지설명
  private String locationPhoto;    // 여행지사진
  private String locationTel;      // 연락처
  private String locationAddr;     // 주소
  private Double locationX;        // 위도
  private Double locationY;        // 경도
  private Integer locationtypeNo;
}
