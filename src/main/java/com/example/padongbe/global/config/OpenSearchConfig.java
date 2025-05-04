package com.example.padongbe.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OpenSearchConfig {

    @Bean(name = "openSearchWebClient")
    public WebClient openSearchWebClient() {
        return WebClient.builder()
                .baseUrl("https://vpc-padong-drraoethwtlm6qjnft7kfjs53e.ap-northeast-2.es.amazonaws.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
