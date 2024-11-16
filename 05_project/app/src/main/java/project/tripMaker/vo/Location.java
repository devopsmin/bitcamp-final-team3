package project.tripMaker.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Random;

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

  public Location() {
    long timestamp = System.currentTimeMillis() % 100000; // 5자리
    int random = new Random().nextInt(1000); // 3자리
    this.locationNo =  (int)(timestamp * 1000 + random); // 8자리
  }
}


//function generateUniqueId() {
//  const timestamp = Date.now() % 100000; // 5자리
//  const random = Math.floor(Math.random() * 1000); // 3자리
//  return timestamp * 1000 + random; // 8자리
//}
