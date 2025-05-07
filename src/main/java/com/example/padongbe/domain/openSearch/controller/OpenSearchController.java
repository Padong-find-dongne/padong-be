package com.example.padongbe.domain.openSearch.controller;

import com.example.padongbe.domain.openSearch.service.OpenSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/open-search")
@Tag(name = "OpenSearch", description = "검색어 자동완성 관련 API")
public class OpenSearchController {
    private final OpenSearchService openSearchService;

    @GetMapping("/keyword")
    @Operation(summary = "자동완성으로 주소 검색")
    @Parameter(name = "keyword", description = "키워드 단어 포함되는 자치구 및 행정동 검색 (초성으로 행정동 검색 가능", example = "서대문, 서대문구, 연희동, 남가좌...")
    public ResponseEntity<List<String>> autoComplete(@RequestParam String keyword) {
        return ResponseEntity.ok(openSearchService.autocomplete(keyword));
    }

    @GetMapping("/create")
    @Operation(summary = "검색 인덱스 매핑")
    public ResponseEntity<String> createAdminDongIndex() {
        openSearchService.createAdminDongIndex();
        return ResponseEntity.ok("인덱스 맵핑 생성 완료");
    }

    @GetMapping("/data")
    @Operation(summary = "검색 데이터 삽입")
    public ResponseEntity<String> insertAdminDongData() {
        openSearchService.uploadBulkFromFile();
        return ResponseEntity.ok("데이터 삽입 완료");
    }

    @GetMapping("/delete")
    @Operation(summary = "검색 데이터 삭제")
    public ResponseEntity<String> deleteAdminDongData() {
        openSearchService.deleteAdminDongIndex();
        return ResponseEntity.ok("인덱스 삭제 완료");
    }

}
