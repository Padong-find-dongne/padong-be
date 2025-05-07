package com.example.padongbe.domain.openSearch.service;

import com.example.padongbe.domain.openSearch.dto.response.OpenSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenSearchService {

    @Qualifier("openSearchWebClient")
    private final WebClient openSearchWebClient;

    public List<String> autocomplete(String keyword) {
        String query;

        if (isChosungOnly(keyword)) {
            query = """
                {
                  "query": {
                    "match": {
                      "admin_area_name_chosung": "%s"
                    }
                  }
                }
            """.formatted(keyword);
        } else {
            query = """
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

        return openSearchWebClient.post()
                .uri("/admin-dong/_search")
                .bodyValue(query)
                .retrieve()
                .bodyToMono(OpenSearchDto.class)
                .map(res -> res.getHits().getHits().stream()
                        .map(hit -> {
                            var src = hit.getSource();
                            return src.getCity() + " " + src.getDistrict() + " " + src.getAdminAreaName();
                        })
                        .toList()
                )
                .block();
    }

    private boolean isChosungOnly(String keyword) {
        return keyword.chars().allMatch(c -> c >= 0x3131 && c <= 0x314E); // ㄱ~ㅎ
    }
}
