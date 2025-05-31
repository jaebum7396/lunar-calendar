package com.codism.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${spring.application.name:user}")
    private String serviceName;

    @Value("${server.port:8081}") // application.properties에서 포트 설정을 가져옴, 기본값은 8081
    private String port; // 로컬 개발용 포트

    @Bean
    public OpenAPI openAPI() {
        // Bearer 토큰 인증 스키마 정의
        SecurityScheme bearerAuth = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        // 보안 요구사항 추가
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                .info(
                        new Info()
                                .title("USER API")
                                .description("API 문서")
                                .version("1.0.0")
                                .contact(
                                        new Contact()
                                                .name("주재범")
                                                .email("jaebum7396@naver.com")
                                )
                )
                .servers(List.of(
                        new Server().url("http://localhost:"+port).description("로컬 서버"),
                        new Server().url("http://jaebum7396.iptime.org:8000/"+serviceName).description("프로덕션 서버")
                ))
                // 보안 스키마 컴포넌트 추가
                .components(new Components().addSecuritySchemes("bearerAuth", bearerAuth))
                // 글로벌 보안 요구사항 추가
                .addSecurityItem(securityRequirement);
    }
}