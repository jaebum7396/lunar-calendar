package com.codism.config.error;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.codism.config.error.entity.ApiException;
import com.codism.config.error.entity.ErrorEntity;
import com.codism.config.error.entity.ErrorEntityBody;
import com.codism.config.error.type.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전역 예외 처리 클래스
 * 모든 예외는 여기서 처리됨
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /* @Valid 또는 @Validated 바인딩 에러 처리 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorEntityBody> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);
        BindingResult bindingResult = e.getBindingResult();
        FieldError fieldError = bindingResult.getFieldErrors().get(0);
        return ErrorEntity.status(ErrorCode.BAD_VALID).body(fieldError.getDefaultMessage());
    }

    /* @RequestBody 바인딩 에러 처리 */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorEntityBody> handleBindException(BindException e) {
        log.error("handleBindException", e);
        BindingResult bindingResult = e.getBindingResult();
        FieldError fieldError = bindingResult.getFieldErrors().get(0);
        return ErrorEntity.status(ErrorCode.BAD_VALID).body(fieldError.getField() + fieldError.getDefaultMessage());
    }

    /* API 에러 응답 처리 */
    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<ErrorEntityBody> handleApiException(ApiException e) {
        log.error("ApiException - {}", e.getMessage());
        return ErrorEntity.status(e.getErrorCode()).body(e.getBody());
    }

    /* 런타임 예외 처리 */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorEntityBody> handleRuntimeException(RuntimeException e) {
        if (e instanceof ApiException apiException) {
            return this.handleApiException(apiException);
        }
        if (e.getCause() instanceof ApiException apiException) {
            return this.handleApiException(apiException);
        }
        log.error("handleRuntimeException", e);
        return ErrorEntity.status(ErrorCode.INTERNAL_SERVER_ERROR).body();
    }

    /* Json parse 예외 처리 */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorEntityBody> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("handleHttpMessageNotReadableException", e);

        // enum 값 오류인지 확인
        if (e.getCause() instanceof InvalidFormatException invalidFormatException) {
            if (invalidFormatException.getTargetType() != null && invalidFormatException.getTargetType().isEnum()) {
                return ErrorEntity.status(ErrorCode.BAD_REQUEST).body("허용되는 enum 값이 아닙니다.");
            }
        }

        return ErrorEntity.status(ErrorCode.JSON_PARSE_ERROR).body(null);
    }

    /* 데이터 무결성 예외 처리 */
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<ErrorEntityBody> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        String message = e.getMostSpecificCause().getMessage();
        if (message.startsWith("Duplicate entry")) {
            return ErrorEntity.status(ErrorCode.DUPLICATE_KEY).body();
        }
        if (message.startsWith("Cannot delete or update a parent row")) {
            return ErrorEntity.status(ErrorCode.EXIST_PARENT).body();
        }
        log.error("DataIntegrityViolationException", e);
        return ErrorEntity.status(ErrorCode.INTERNAL_SERVER_ERROR).body();
    }

    /* 전역 예외 처리 */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorEntityBody> handleGlobalException(Exception e) {
        log.error("handleGlobalException", e);
        return ErrorEntity.status(ErrorCode.INTERNAL_SERVER_ERROR).body();
    }
}