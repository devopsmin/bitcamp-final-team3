package project.tripMaker.dto;

import lombok.Data;
import project.tripMaker.vo.Location;

@Data
public class LocationDTO {
  private Integer locationNo;      // JS의 문자열을 Integer로 자동 변환
  private String thirdclassCode;
  private String cityCode;
  private String locationName;
  private String locationDesc;
  private String locationPhoto;
  private String locationTel;
  private String locationAddr;
  private Double locationX;        // JS의 문자열을 Double로 자동 변환
  private Double locationY;
  private Integer locationtypeNo;  // JS의 문자열을 Integer로 자동 변환

  // Location Entity로 변환하는 메소드
  public Location toEntity() {
    Location location = new Location();
    location.setLocationNo(this.locationNo);
    location.setThirdclassCode(this.thirdclassCode);
    location.setCityCode(this.cityCode);
    location.setLocationName(this.locationName);
    location.setLocationDesc(this.locationDesc);
    location.setLocationPhoto(this.locationPhoto);
    location.setLocationTel(this.locationTel);
    location.setLocationAddr(this.locationAddr);
    location.setLocationX(this.locationX);
    location.setLocationY(this.locationY);
    location.setLocationtypeNo(this.locationtypeNo);
    return location;
  }

  // Location Entity를 DTO로 변환하는 정적 메소드
  public static LocationDTO fromEntity(Location location) {
    LocationDTO dto = new LocationDTO();
    dto.setLocationNo(location.getLocationNo());
    dto.setThirdclassCode(location.getThirdclassCode());
    dto.setCityCode(location.getCityCode());
    dto.setLocationName(location.getLocationName());
    dto.setLocationDesc(location.getLocationDesc());
    dto.setLocationPhoto(location.getLocationPhoto());
    dto.setLocationTel(location.getLocationTel());
    dto.setLocationAddr(location.getLocationAddr());
    dto.setLocationX(location.getLocationX());
    dto.setLocationY(location.getLocationY());
    dto.setLocationtypeNo(location.getLocationtypeNo());
    return dto;
  }
}
