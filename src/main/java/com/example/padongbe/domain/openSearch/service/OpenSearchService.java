package com.example.padongbe.domain.openSearch.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
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

    public void createAdminDongIndex() {
        webClient.head()
                .uri("/admin-dong")
                .retrieve()
                .toBodilessEntity()
                .onErrorResume(e -> {
                    return webClient.put()
                            .uri("/admin-dong")
                            .bodyValue(buildIndexSettings())
                            .retrieve()
                            .toBodilessEntity();
                })
                .block();
    }

    private Map<String, Object> buildIndexSettings() {
        return Map.of(
                "settings", Map.of(
                        "analysis", Map.of(
                                "tokenizer", Map.of(
                                        "autocomplete_tokenizer", Map.of(
                                                "type", "edge_ngram",
                                                "min_gram", 1,
                                                "max_gram", 20,
                                                "token_chars", List.of("letter", "digit", "whitespace")
                                        )
                                ),
                                "analyzer", Map.of(
                                        "autocomplete_analyzer", Map.of(
                                                "type", "custom",
                                                "tokenizer", "autocomplete_tokenizer",
                                                "filter", List.of("lowercase")
                                        ),
                                        "chosung_analyzer", Map.of(
                                                "type", "whitespace"
                                        )
                                )
                        )
                ),
                "mappings", Map.of(
                        "properties", Map.of(
                                "id", Map.of("type", "integer"),
                                "admin_area_name", Map.of(
                                        "type", "text",
                                        "analyzer", "autocomplete_analyzer",
                                        "search_analyzer", "standard"
                                ),
                                "admin_area_name_chosung", Map.of(
                                        "type", "text",
                                        "analyzer", "chosung_analyzer"
                                ),
                                "district", Map.of("type", "text"),
                                "city", Map.of("type", "keyword")
                        )
                )
        );
    }

    public void uploadBulkFromFile() {
        try {
            ClassPathResource resource = new ClassPathResource("data/admin_dong_bulk_fixed.json");
            StringBuilder ndjsonBuilder = new StringBuilder();

            try (Scanner scanner = new Scanner(resource.getInputStream(), StandardCharsets.UTF_8)) {
                while (scanner.hasNextLine()) {
                    ndjsonBuilder.append(scanner.nextLine()).append("\n");
                }
            }

            webClient.post()
                    .uri("/_bulk")
                    .header("Content-Type", "application/x-ndjson")
                    .bodyValue(ndjsonBuilder.toString())
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnNext(response -> System.out.println("Bulk upload response: " + response))
                    .onErrorResume(e -> {
                        e.printStackTrace();
                        return Mono.empty();
                    })
                    .block();

        } catch (Exception e) {
            System.err.println("Failed to upload bulk data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteAdminDongIndex() {
        webClient.delete()
                .uri("/admin-dong")
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> System.out.println("Delete response: " + response))
                .onErrorResume(e -> {
                    e.printStackTrace();
                    return Mono.empty();
                })
                .block();
    }
}
