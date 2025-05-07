package com.example.padongbe.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OpenSearchConfig {

    @Value("${open-search.url}")
    private String openSearchUrl;

    @Bean(name = "openSearchWebClient")
    public WebClient openSearchWebClient(AwsSigV4SignerFilter signerFilter) {
        return WebClient.builder()
                .baseUrl(openSearchUrl)
                .filter(signerFilter.awsSigner())
                .build();
    }
}
