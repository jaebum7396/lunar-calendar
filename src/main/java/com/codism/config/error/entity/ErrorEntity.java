package com.codism.config.error.entity;

import com.codism.config.error.type.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

/**
 * API 응답 엔티티 생성
 */
@Slf4j
public class ErrorEntity<T> extends ResponseEntity<T> {

    public ErrorEntity(HttpStatus status) {
        super(status);
    }

    public ErrorEntity(T body, HttpStatus status) {
        super(body, status);
    }

    public ErrorEntity(MultiValueMap<String, String> headers, HttpStatus status) {
        super(headers, status);
    }

    public ErrorEntity(T body, MultiValueMap<String, String> headers, HttpStatus status) {
        super(body, headers, status);
    }

    public ErrorEntity(T body, MultiValueMap<String, String> headers, int rawStatus) {
        super(body, headers, rawStatus);
    }

    public static EntityBuilder status(ErrorCode errorCode) {
        return new EntityBuilder(errorCode);
    }

    public static EntityBuilder status(String code, HttpStatus status) {
        return new EntityBuilder(code, status);
    }

    public static class EntityBuilder {
        private final String code;
        private final HttpStatus status;

        public EntityBuilder(String code, HttpStatus status) {
            this.code = code;
            this.status = status;
        }

        public EntityBuilder(ErrorCode errorCode) {
            this.code = errorCode.name();
            this.status = errorCode.getStatus();
        }

        public ResponseEntity<ErrorEntityBody> body() {
            return body(null);
        }

        public ResponseEntity<ErrorEntityBody> body(Object o) {
            ErrorEntityBody errorEntityBody = new ErrorEntityBody(code, o);

            return status(status).body(errorEntityBody);
        }
    }
}
