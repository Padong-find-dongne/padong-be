package com.example.padongbe.boundary.service;

import com.example.padongbe.boundary.dto.BoundaryResponse;
import com.example.padongbe.global.ResponseDTO;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class BoundaryService {
  private final WebClient webClient = WebClient.builder()
      .baseUrl("http://api.vworld.kr")
      .build();

  private final String serviceKey= "E1D96B74-5EBD-37FC-9FCE-78648323050F";

  private final Gson gson = new Gson();

  public ResponseDTO<BoundaryResponse> getBoundary(String geocode) {
    String jsonString = webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/req/data")
            .queryParam("service", "data")
            .queryParam("request", "GetFeature")
            .queryParam("data", "LT_C_ADEMD_INFO")
            .queryParam("key", serviceKey)
            .queryParam("domain", "www.padong.site")
            .queryParam("attrFilter", "emdCd:=:" + geocode)
            .build())
        .retrieve()
        .bodyToMono(String.class)
        .block(); // (주의: block()은 동기적으로 응답 대기)

    // 이제 Gson으로 파싱
    JsonObject root = gson.fromJson(jsonString, JsonObject.class);

    JsonArray coordinates = root.getAsJsonObject("response")
        .getAsJsonObject("result")
        .getAsJsonObject("featureCollection")
        .getAsJsonArray("features")
        .get(0)
        .getAsJsonObject()
        .getAsJsonObject("geometry")
        .getAsJsonArray("coordinates")
        .get(0)
        .getAsJsonArray()
        .get(0)
        .getAsJsonArray();

    List<List<Double>> points = new ArrayList<>();
    for (JsonElement coord : coordinates) {
      JsonArray pair = coord.getAsJsonArray();
      double lon = pair.get(0).getAsDouble(); // 경도
      double lat = pair.get(1).getAsDouble(); // 위도

      List<Double> point = new ArrayList<>();
      point.add(lon);
      point.add(lat);
      points.add(point);

    }
    BoundaryResponse response= new BoundaryResponse(points);
    return ResponseDTO.res(HttpStatus.OK, "조회 성공", response);
  }
}
