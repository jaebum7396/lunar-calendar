package com.codism.config.error.entity;

import com.codism.config.error.type.ErrorCode;
import lombok.Getter;

/**
 * API 에러 응답을 위한 RuntimeException 확장 클래스
 */
@Getter
public class ApiException extends RuntimeException {
    private final ErrorCode errorCode;
    private Object body;

    public ApiException(ErrorCode responseCode, Object body) {
        super(responseCode.name(), null, false, false);
        this.errorCode = responseCode;
        this.body = body;
    }

    public ApiException(ErrorCode errorCode) {
        super(errorCode.name(), null, false, false);
        this.errorCode = errorCode;
        this.body = errorCode.getMessage();
    }
}
