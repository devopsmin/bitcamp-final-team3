package project.tripMaker.vo;

import lombok.Data;

@Data
public class City {
    private State state;
    private String cityCode;
    private String cityName;
    private String cityPhoto;
}
