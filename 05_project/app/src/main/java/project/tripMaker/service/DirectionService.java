package project.tripMaker.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.stereotype.Service;

import project.tripMaker.vo.RouteInfo;
import project.tripMaker.vo.Schedule;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class DirectionService {
  private final String API_KEY_ID = "0zojwabqn9";
  private final String API_KEY = "mtgHl2KSLnNK03kqcBAYe6TpMD4DsWGVHstoRpvA";
  private final String BASE_URL = "https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving";
  private final ObjectMapper objectMapper = new ObjectMapper();

  public RouteInfo getDirection(Schedule start, Schedule goal) {
    double startLon = start.getLocation().getLocationX();
    double startLat = start.getLocation().getLocationY();
    double goalLon = goal.getLocation().getLocationX();
    double goalLat = goal.getLocation().getLocationY();

    try {
      String params = String.format("start=%.7f,%.7f&goal=%.7f,%.7f&option=trafast",
          startLon, startLat, goalLon, goalLat);

      JsonNode response = requestApi(BASE_URL, params);
      int code = response.get("code").asInt();

      // code가 1인 경우 기본값을 가진 RouteInfo 반환
      if (code == 1) {
        RouteInfo info = new RouteInfo(
            new RouteInfo.Coordinates(startLon, startLat),  // 시작 좌표
            new RouteInfo.Coordinates(goalLon, goalLat),    // 도착 좌표
            0,       // distance = 0
            0,       // duration = 0
            null    // path = null
        );

        return info;
      }

      // code가 1이 아닌 경우 정상적으로 파싱
      return parseResponse(response);

    } catch (Exception e) {
      throw new RuntimeException("Failed to get direction", e);
    }
  }

  private RouteInfo parseResponse(JsonNode responseJson) {
    try {
      JsonNode route = responseJson.get("route").get("trafast").get(0);
      JsonNode summary = route.get("summary");

      // Start Coordinates
      JsonNode startLocation = summary.get("start").get("location");
      RouteInfo.Coordinates start = new RouteInfo.Coordinates(
          startLocation.get(0).asDouble(),
          startLocation.get(1).asDouble()
      );

      // Goal Coordinates
      JsonNode goalLocation = summary.get("goal").get("location");
      RouteInfo.Coordinates goal = new RouteInfo.Coordinates(
          goalLocation.get(0).asDouble(),
          goalLocation.get(1).asDouble()
      );

      // Distance and duration
      int distance = summary.get("distance").asInt();
      int duration = summary.get("duration").asInt();

      // Path coordinates
      List<RouteInfo.Coordinates> path = new ArrayList<>();
      JsonNode pathArray = route.get("path");
      for (JsonNode point : pathArray) {
        path.add(new RouteInfo.Coordinates(
            point.get(0).asDouble(),
            point.get(1).asDouble()
        ));
      }

      return new RouteInfo(start, goal, distance, duration, path);

    } catch (Exception e) {
      throw new RuntimeException("Failed to parse direction response", e);
    }
  }

  private JsonNode requestApi(String requestURL, String params) throws Exception {
    StringBuilder urlBuilder = new StringBuilder(requestURL);
    urlBuilder.append("?").append(params);

    URL url = new URL(urlBuilder.toString());
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

    conn.setRequestMethod("GET");
    conn.setRequestProperty("x-ncp-apigw-api-key-id", API_KEY_ID);
    conn.setRequestProperty("x-ncp-apigw-api-key", API_KEY);

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

    return objectMapper.readTree(sb.toString());
  }
}




