package project.tripMaker.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import project.tripMaker.vo.City;
import project.tripMaker.vo.Location;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class TourAPIService {

  private final String apiKey =
          "XnvCfqp1B9c3utiwpFbA7r1fL/VB+ajinxxnfzDqa9hYdr4tHh1sf0WUcxbFXSyK1HSCHQ3Llk6QLVRC9yMJ2g==";
  private final ObjectMapper objectMapper = new ObjectMapper();

  public static void main(String[] args) {
    TourAPIService tourAPIService = new TourAPIService();
    tourAPIService.stateList();
  }

  // 공통 API 요청 메서드
  private JsonNode requestApi(String requestURL, String params) throws Exception {
    StringBuilder urlBuilder = new StringBuilder(requestURL);
    urlBuilder.append("?").append("serviceKey=").append(URLEncoder.encode(apiKey, "UTF-8"));
    urlBuilder.append("&").append(params);
    URL url = new URL(urlBuilder.toString());

    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");

    BufferedReader rd;
    if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
      rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    } else {
      rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
    }

    StringBuilder sb = new StringBuilder();
    String line;
    while ((line = rd.readLine()) != null) {
      sb.append(line);
    }
    rd.close();
    conn.disconnect();
    System.out.println(sb.toString());
    return objectMapper.readTree(sb.toString());
  }

  public JsonNode stateList() {
    try {
      String requestURL = "https://apis.data.go.kr/B551011/KorService1/areaCode1";
      String params = "numOfRows=100&pageNo=1&MobileOS=ETC&MobileApp=webapp&_type=json";
      JsonNode itemsNode =
              requestApi(requestURL, params).path("response").path("body").path("items").path("item");
      return itemsNode;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public List<City> cityList(String stateCode) {
    try {
      String requestURL = "https://apis.data.go.kr/B551011/KorService1/areaCode1";
      String params =
              "numOfRows=100&pageNo=1&MobileOS=ETC&MobileApp=webapp&_type=json&areaCode=" + stateCode;
      JsonNode itemsNode =
              requestApi(requestURL, params).path("response").path("body").path("items").path("item");

      List<City> cityList = new ArrayList<>();
      City totalCity = new City();
      totalCity.setCityCode("");
      totalCity.setCityName("전체");
      cityList.add(totalCity);

      if (itemsNode.isArray()) {
        for (JsonNode itemNode : itemsNode) {
          City city = new City();
          city.setCityCode(itemNode.path("code").asText());
          city.setCityName(itemNode.path("name").asText());
          cityList.add(city);
        }
      }
      return cityList;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public List<Location> showLocation(City city) {
    try {
      String requestURL = "https://apis.data.go.kr/B551011/KorService1/areaBasedList1";
      String params = String.format(
              "numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=MobileApp&_type=json&listYN=Y&arrange=O&contentTypeId=12&areaCode=%s&sigunguCode=%s",
              city.getState().getStateTour(), city.getCityTour());
      JsonNode itemsNode =
              requestApi(requestURL, params).path("response").path("body").path("items").path("item");

      List<Location> locations = new ArrayList<>();
      if (itemsNode.isArray()) {
        for (JsonNode itemNode : itemsNode) {
          Location location = new Location();
          location.setLocationName(itemNode.path("title").asText());
          location.setLocationAddr(itemNode.path("addr1").asText("-"));
          location.setThirdclassCode(itemNode.path("cat3").asText());
          location.setLocationPhoto(itemNode.path("firstimage").asText(null));
          location.setLocationTel(itemNode.path("tel").asText(null));
          location.setLocationX(itemNode.path("mapx").asDouble());
          location.setLocationY(itemNode.path("mapy").asDouble());
          location.setLocationtypeNo(1);
          locations.add(location);
          System.out.println(location);
        }
      }
      return locations;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public List<Location> showHotel(City city) {
    try {
      String requestURL = "https://apis.data.go.kr/B551011/KorService1/areaBasedList1";
      String params = String.format(
              "numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=MobileApp&_type=json&listYN=Y&arrange=O&contentTypeId=32&areaCode=%s&sigunguCode=%s",
              city.getState().getStateTour(), city.getCityTour());
      JsonNode itemsNode =
              requestApi(requestURL, params).path("response").path("body").path("items").path("item");

      List<Location> locations = new ArrayList<>();
      if (itemsNode.isArray()) {
        for (JsonNode itemNode : itemsNode) {
          Location location = new Location();
          location.setLocationName(itemNode.path("title").asText());
          location.setLocationAddr(itemNode.path("addr1").asText("-"));
          location.setThirdclassCode(itemNode.path("cat3").asText());
          location.setLocationPhoto(itemNode.path("firstimage").asText(null));
          location.setLocationTel(itemNode.path("tel").asText(null));
          location.setLocationX(itemNode.path("mapx").asDouble());
          location.setLocationY(itemNode.path("mapy").asDouble());
          locations.add(location);
        }
      }
      return locations;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
