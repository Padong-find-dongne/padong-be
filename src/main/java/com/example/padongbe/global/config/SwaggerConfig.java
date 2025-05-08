package com.example.padongbe.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${swagger-config.server-url-http}")
    private String httpServerUrl;

    @Value("${swagger-config.server-url-https}")
    private String httpsServerUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .servers(serverList());
    }

    private Info apiInfo() {
        return new Info()
                .title("Padong API")
                .description("2025 서울시 공공데이터 활용 창업경진대회")
                .version("1.0");
    }

    private List<Server> serverList() {
        return List.of(
                new Server()
                        .url(httpServerUrl)
                        .description("HTTP Server"),
                new Server()
                        .url(httpsServerUrl)
                        .description("HTTPS Server")
        );
    }
}
