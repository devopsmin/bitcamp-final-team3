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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchAPIService {

  private final String BASE_URL = "https://openapi.naver.com/v1/search/local.json";
  private final String clientId = "vvcE5GftCUO4WxBGC52I"; //애플리케이션 클라이언트 아이디
  private final String clientSecret = "HFm7bQSO3f"; //애플리케이션 클라이언트 시크릿
  private final ObjectMapper objectMapper = new ObjectMapper();

  // 공통 API 요청 메서드
  private JsonNode requestApi(URL requestURL, Map<String, String> requestHeaders) throws Exception {
    HttpURLConnection conn = (HttpURLConnection) requestURL.openConnection();

    conn.setRequestMethod("GET");
    for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
      conn.setRequestProperty(header.getKey(), header.getValue());
    }

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

  public List<Location> search(String searchText) {
    try {
      String text = URLEncoder.encode(searchText, "utf-8");
      String requestAPI = String.format(BASE_URL+"?query='%s'&display=5&sort=random",text);
      URL url = new URL(requestAPI);

      Map<String, String> requestHeaders = new HashMap<>();
      requestHeaders.put("X-Naver-Client-Id", clientId);
      requestHeaders.put("X-Naver-Client-Secret", clientSecret);
      JsonNode responseBody = requestApi(url,requestHeaders);
      JsonNode itemsNode = responseBody.path("items");

//      if (itemsNode.isEmpty()) {
//        return null;
//      }

      List<Location> locations = new ArrayList<>();
      for (JsonNode itemNode : itemsNode) {
        Location location = new Location();
        location.setLocationName(itemNode.path("title").asText());
        location.setLocationAddr(itemNode.path("roadAddress").asText());
        location.setLocationX(itemNode.path("mapx").asDouble() / 10000000.0);
        location.setLocationY(itemNode.path("mapy").asDouble() / 10000000.0);
        locations.add(location);

      }
      return locations;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
