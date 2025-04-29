package com.example.padongbe.domain.news.controller;

import com.example.padongbe.domain.news.dto.NewsResponse;
import com.example.padongbe.domain.news.service.NewsService;
import com.example.padongbe.global.ResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
@Tag(name = "News", description = "뉴스 정보 요청 API")
public class NewsController {

  private final NewsService newsService;

  @GetMapping()
  public ResponseEntity<ResponseDTO<NewsResponse>> getNews(){
      return ResponseEntity.ok(newsService.getNews());
  }
}
