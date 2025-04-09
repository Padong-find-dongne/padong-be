package com.example.padongbe.global.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

// http://localhost:8080/swagger-ui/index.html
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(info())
                .servers(servers());
    }

    private Info info() {
        return new Info()
                .title("Padong API")
                .description("2025 서울시 공공데이터 활용 창업경진대회")
                .version("1.0");
    }

    private List<Server> servers() {
        return List.of(
                new Server()
                    .url("http://3.39.234.97:8080")
                    .description("Configured Server"),
                new Server()
                    .url("http://localhost:8080")
                    .description("Local Server")
        );
    }

}
