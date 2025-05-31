package com.codism.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Value("${cloud.gateway.uri}")
	String GATEWAY_URI;
	@Value("${spring.profiles.active}")
	String ACTIVE_PROFILE;

	private final ObjectMapper objectMapper;

	@Autowired
	public WebMvcConfig(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public void addInterceptors(InterceptorRegistry reg) {
		reg.addInterceptor(new MyInterceptor(GATEWAY_URI, ACTIVE_PROFILE, objectMapper))
				.addPathPatterns("/**")
				.excludePathPatterns(
						"/css/**", "/images/**", "/js/**", "/swagger-ui/**",
						"/actuator/**", "/v3/api-docs/**"
				);
	}

	/**
	 * <h3>CORS 설정</h3>
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		if(ACTIVE_PROFILE.equals("local")) {
			/*registry.addMapping("/**") // 모든 경로에 대해 CORS 설정 적용
				.allowedOrigins(
						"http://localhost:3000"  // React 개발 서버
						,"http://localhost:5173"
						,"http://jaebum7396.iptime.org:5173"
						,"http://localhost:8080"
						,"http://localhost:8004"
						,"http://localhost:3001"  // React 개발 서버
						,"http://jaebum7396.iptime.org:3001" // Spring Boot 개발 서버
						,GATEWAY_URI
				)
				.allowedOrigins("*") // 허용할 도메인을 allowedOrigins에 지정
				.allowedMethods("*") // 허용할 HTTP 메서드 설정
				.allowedHeaders("*") // 허용할 헤더 설정
				;*/
		}
	}
}
