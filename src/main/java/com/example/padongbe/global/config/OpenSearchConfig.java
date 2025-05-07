package com.example.padongbe.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.opensearch.OpenSearchClient;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;

import java.net.URI;

@Configuration
public class OpenSearchConfig {

    @Value("${open-search.url}")
    private String openSearchUrl;

    @Value("${open-search.iam.region}")
    private String region;

    @Value("${open-search.iam.access-key}")
    private String accessKey;

    @Value("${open-search.iam.secret-key}")
    private String secretKey;

    @Value("${open-search.iam.session-token}")
    private String sessionToken;

    @Bean
    public OpenSearchClient openSearchClient() {
        AwsSessionCredentials credentials = AwsSessionCredentials.create(
                accessKey,
                secretKey,
                sessionToken
        );

        return OpenSearchClient.builder()
                .region(Region.of(region))
                .endpointOverride(URI.create(openSearchUrl))
                .httpClient(UrlConnectionHttpClient.create())
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }
}