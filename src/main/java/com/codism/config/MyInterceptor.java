package com.codism.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.util.Enumeration;

@Slf4j
public class MyInterceptor implements HandlerInterceptor {
	private final String GATEWAY_URI;
	private final String ACTIVE_PROFILE;
	private final ObjectMapper objectMapper;

	public MyInterceptor(String GATEWAY_URI, String ACTIVE_PROFILE,
						 ObjectMapper objectMapper) {
		this.GATEWAY_URI = GATEWAY_URI;
		this.ACTIVE_PROFILE = ACTIVE_PROFILE;
		this.objectMapper = objectMapper;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		log.info("===============================================");
		log.info("==================== BEGIN ====================");

		// 헤더 정보 로깅
		Enumeration<String> eHeader = request.getHeaderNames();
		while (eHeader.hasMoreElements()) {
			String key = eHeader.nextElement();
			String value = request.getHeader(key);
			log.info("key : " + key + " ===> value : " + value);
		}

		String requestUri = request.getHeader("x-forwarded-host");
		log.info("ACTIVE_PROFILE : " + ACTIVE_PROFILE);
		log.info("GATEWAY_URI : " + GATEWAY_URI);

		if (ACTIVE_PROFILE.equals("local")) {
			log.info("로컬에서 실행중입니다.");
		} else {
			// 프로덕션 환경 관련 코드
		}

		// 요청 로깅 객체 생성
		try {
			// 클라이언트 정보 수집
			String ipAddress = getClientIp(request);
			String deviceType = determineDeviceType(request);
			String deviceId = request.getHeader("X-Device-ID");
			String endpoint = request.getRequestURI();

			// 쿼리 파라미터만 추출
			JsonNode paramsNode = extractRequestParams(request);
			log.info("요청 파라미터: {}", paramsNode.toPrettyString());
		} catch (Exception e) {
			log.error("요청 로그 객체 생성 실패: {}", e.getMessage(), e);
		}

		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

	private JsonNode extractRequestParams(HttpServletRequest request) {
		ObjectNode paramsNode = objectMapper.createObjectNode();

		try {
			// 1. 쿼리 파라미터 추출
			request.getParameterMap().forEach((paramName, paramValues) -> {
				if (paramValues.length == 1) {
					paramsNode.put(paramName, paramValues[0]);
				} else if (paramValues.length > 1) {
					ArrayNode valueArray = paramsNode.putArray(paramName);
					for (String value : paramValues) {
						valueArray.add(value);
					}
				}
			});

			// 2. 요청 본문 추출 시도 (POST, PUT 등)
			if (isJsonRequest(request) && request instanceof ContentCachingRequestWrapper) {
				ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;
				byte[] buf = wrapper.getContentAsByteArray();
				if (buf.length > 0) {
					String body = new String(buf, wrapper.getCharacterEncoding());
					JsonNode bodyNode = objectMapper.readTree(body);
					paramsNode.set("body", bodyNode);
				}
			}

			return paramsNode;
		} catch (Exception e) {
			log.warn("파라미터 추출 실패: {}", e.getMessage());
			return objectMapper.createObjectNode();
		}
	}

	/**
	 * 요청이 JSON인지 확인합니다.
	 */
	private boolean isJsonRequest(HttpServletRequest request) {
		String contentType = request.getContentType();
		return contentType != null && contentType.contains("application/json");
	}

	// 클라이언트 IP 주소 추출
	private String getClientIp(HttpServletRequest request) {
		String ipAddress = request.getHeader("X-Forwarded-For");
		if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
		}
		return ipAddress;
	}

	// 디바이스 타입 결정
	private String determineDeviceType(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		if (userAgent == null) return "UNKNOWN";

		userAgent = userAgent.toLowerCase();
		if (userAgent.contains("mobile") || userAgent.contains("android") || userAgent.contains("iphone")) {
			return "MOBILE";
		} else {
			return "WEB";
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		log.info("==================== END ======================");
		log.info("===============================================");
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
}
