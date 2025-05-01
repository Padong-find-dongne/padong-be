package com.example.padongbe.domain.news.service;

import com.example.padongbe.domain.news.dto.NewsResponse;
import com.example.padongbe.global.ResponseDTO;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

@Service
@Slf4j
public class NewsService {
  private final String BASE_URL = "https://openapi.naver.com";
  private final String serviceKey = "OE2sHIk6MRnEbwBG8PuV";
  private final String secretKey = "clvSRTarvD";
  private final WebClient webClient = WebClient.builder()
      .baseUrl(BASE_URL)
      .defaultHeader("X-Naver-Client-Id", serviceKey)
      .defaultHeader("X-Naver-Client-Secret", secretKey)
      .build();

  public String extractThumbnail(String originallink) {
    try {
      Document doc = Jsoup.connect(originallink)
          .userAgent("Mozilla")  // 뉴스 사이트에서 봇 차단 피하기
          .timeout(3000)
          .get();

      Element metaImage = doc.selectFirst("meta[property=og:image]");
      if (metaImage != null) {
        return metaImage.attr("content");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public ResponseDTO<NewsResponse> getNews(String search) {

    NewsResponse response = webClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/v1/search/news.json")
            .queryParam("query", search)
            .queryParam("display", 10)
            .build())
        .retrieve()
        .bodyToMono(NewsResponse.class)
        .block();

    assert response != null;
    response.getItems().forEach(item -> {
      item.setTitle(cleanHtml(item.getTitle()));
      item.setDescription(cleanHtml(item.getDescription()));
      item.setThumbnail(extractThumbnail(item.getOriginallink()));
    });


    return ResponseDTO.res(HttpStatus.OK,"news 조회 성공",response);
  }

  private String cleanHtml(String html) {
    return Jsoup.parse(html).text();
  }
}