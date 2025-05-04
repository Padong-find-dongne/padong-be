package com.example.padongbe.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OpenSearchConfig {

    @Value("${open-search.username}")
    private String username;
    @Value("${open-search.password}")
    private String password;
    @Value("${open-search.url}")
    private String url;

    @Bean(name = "openSearchWebClient")
    public WebClient openSearchWebClient() {
        return WebClient.builder()
                .baseUrl(url)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.setBasicAuth(username, password);
                    httpHeaders.setContentType(MediaType.APPLICATION_JSON);})
                .build();
    }
}
