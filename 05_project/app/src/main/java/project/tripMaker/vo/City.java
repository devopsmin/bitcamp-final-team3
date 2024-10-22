package project.tripMaker.vo;

public class City {
    private State state;
    private String cityCode;
    private String cityName;
    private String cityPhoto;

  private State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public String getCityCode() {
    return cityCode;
  }

  public void setCityCode(String cityCode) {
    this.cityCode = cityCode;
  }

  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public String getCityPhoto() {
    return cityPhoto;
  }

  public void setCityPhoto(String cityPhoto) {
    this.cityPhoto = cityPhoto;
  }
}
