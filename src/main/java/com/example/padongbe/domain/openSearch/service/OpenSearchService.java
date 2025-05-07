package com.example.padongbe.domain.openSearch.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class OpenSearchService {

    @Qualifier("openSearchWebClient")
    private final WebClient webClient;

    public List<String> autocomplete(String keyword) {
        String query = buildQuery(keyword);

        return webClient.post()
                .uri("/admin-dong/_search")
                .bodyValue(query)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(json -> {
                    JsonNode hits = json.path("hits").path("hits");
                    return StreamSupport.stream(hits.spliterator(), false)
                            .map(hit -> hit.path("_source"))
                            .map(src -> src.path("city").asText() + " " +
                                    src.path("district").asText() + " " +
                                    src.path("admin_area_name").asText())
                            .collect(Collectors.toList());
                })
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.just(List.of()); // 실패 시 빈 리스트 반환
                })
                .block();
    }

    private String buildQuery(String keyword) {
        if (isChosungOnly(keyword)) {
            return """
                {
                  "query": {
                    "match": {
                      "admin_area_name_chosung": "%s"
                    }
                  }
                }
            """.formatted(keyword);
        } else {
            return """
                {
                  "query": {
                    "multi_match": {
                      "query": "%s",
                      "fields": ["district", "admin_area_name"]
                    }
                  }
                }
            """.formatted(keyword);
        }
    }

    private boolean isChosungOnly(String keyword) {
        return keyword.chars().allMatch(c -> c >= 0x3131 && c <= 0x314E);
    }
}
