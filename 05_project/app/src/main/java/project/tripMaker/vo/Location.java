package project.tripMaker.vo;

public class Location {
  private Integer locationNo;      // 여행지정보 번호
  private Integer thirdclassNo;    // 소분류번호
  private String locationName;     // 여행지이름
  private String locationDesc;     // 여행지설명
  private String locationPhoto;    // 여행지사진
  private String locationTel;      // 연락처
  private String locationAddr;     // 주소
  private Double locationX;        // 위도
  private Double locationY;        // 경도
  private String cityCode;
  private String stateCode;


  public Integer getLocationNo() {
    return locationNo;
  }

  public void setLocationNo(Integer locationNo) {
    this.locationNo = locationNo;
  }

  public Integer getThirdclassNo() {
    return thirdclassNo;
  }

  public void setThirdclassNo(Integer thirdclassNo) {
    this.thirdclassNo = thirdclassNo;
  }

  public String getLocationName() {
    return locationName;
  }

  public void setLocationName(String locationName) {
    this.locationName = locationName;
  }

  public String getLocationDesc() {
    return locationDesc;
  }

  public void setLocationDesc(String locationDesc) {
    this.locationDesc = locationDesc;
  }

  public String getLocationPhoto() {
    return locationPhoto;
  }

  public void setLocationPhoto(String locationPhoto) {
    this.locationPhoto = locationPhoto;
  }

  public String getLocationTel() {
    return locationTel;
  }

  public void setLocationTel(String locationTel) {
    this.locationTel = locationTel;
  }

  public String getLocationAddr() {
    return locationAddr;
  }

  public void setLocationAddr(String locationAddr) {
    this.locationAddr = locationAddr;
  }

  public Double getLocationX() {
    return locationX;
  }

  public void setLocationX(Double locationX) {
    this.locationX = locationX;
  }

  public Double getLocationY() {
    return locationY;
  }

  public void setLocationY(Double locationY) {
    this.locationY = locationY;
  }

  public String getCityCode() {
    return cityCode;
  }

  public void setCityCode(String cityCode) {
    this.cityCode = cityCode;
  }

  public String getStateCode() {
    return stateCode;
  }

  public void setStateCode(String stateCode) {
    this.stateCode = stateCode;
  }
}
