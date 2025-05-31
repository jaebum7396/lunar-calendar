package com.codism.config.error.type;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 400: Bad Request 계열
    BAD_REQUEST(400, HttpStatus.BAD_REQUEST, "잘못된 요청입니다"),
    BAD_VALID(400, HttpStatus.BAD_REQUEST, "입력값 검증에 실패했습니다"),
    DUPLICATE_KEY(400, HttpStatus.BAD_REQUEST, "이미 존재하는 데이터입니다"),
    EXIST_PARENT(400, HttpStatus.BAD_REQUEST, "참조된 데이터가 있어 삭제할 수 없습니다"),
    JSON_PARSE_ERROR(400, HttpStatus.UNAUTHORIZED, "JSON Parse Error"),
    SOLD_OUT(400, HttpStatus.BAD_REQUEST, "상품의 재고가 부족합니다"),
    DUPLICATE_PRODUCT_CODE(400, HttpStatus.BAD_REQUEST, "중복된 상품 코드"),
    NOT_REFUND(400, HttpStatus.BAD_REQUEST, "환불 불가"),
    NOT_DELIVERED(400, HttpStatus.BAD_REQUEST, "배송 불가"),

    // 404: Not Found
    NOT_FOUND(404, HttpStatus.NOT_FOUND, "해당 정보를 찾을 수 없습니다"),
    PRODUCT_CODE_NOT_FOUND(404, HttpStatus.NOT_FOUND, "존재하지 않는 상품 코드 입니다."),
    PRODUCT_SIZE_NOT_FOUND(404, HttpStatus.NOT_FOUND, "존재하지 않는 상품 사이즈 입니다."),
    ORDERS_NOT_FOUND(404, HttpStatus.NOT_FOUND, "존재하지 않는 주문 입니다."),
    ORDER_PRODUCT_NOT_FOUND(404, HttpStatus.NOT_FOUND, "존재하지 않는 주문 상품 입니다."),

    USER_NOT_FOUND(4041, HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"),
    LOGIN_FAILED(4042, HttpStatus.NOT_FOUND, "로그인에 실패했습니다"),

    // 408: Time Out
    TIME_OUT(408, HttpStatus.REQUEST_TIMEOUT, "요청 시간이 초과되었습니다"),

    // 500: Internal Server Error
    INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다"),

    ;

    private final int code;
    private final HttpStatus status;
    private final String message;

    ErrorCode(final int code, final HttpStatus status, final String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }
}
