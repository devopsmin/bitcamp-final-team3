package project.tripMaker.vo;

import lombok.Data;
import lombok.Getter;

@Data
public class City {
    private State state;
    private String cityCode;
    private String cityName;
    private String cityPhoto;
}
