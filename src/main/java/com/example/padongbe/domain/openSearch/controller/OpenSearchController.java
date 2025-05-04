package com.example.padongbe.domain.openSearch.controller;

import com.example.padongbe.domain.openSearch.service.OpenSearchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/open-search")
@Tag(name = "OpenSearch API", description = "검색어 자동완성 관련 API")
public class OpenSearchController {
    private final OpenSearchService openSearchService;

    @GetMapping
    public List<String> autoComplete(@RequestParam String keyword) {
        return openSearchService.autocomplete(keyword);
    }

}
