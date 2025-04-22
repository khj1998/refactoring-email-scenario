package com.email.common;

import lombok.Getter;

@Getter
public enum StatusCodeEnum {
    EMAIL_QUERY_SUCCESS("1000","email inquiry was successful"),
    EMAIL_SEND_SUCCESS("1001","email has been sent successfully"),

    SYSTEM_NOT_EXISTS("3000","system has not been found"),
    EMAIL_NOT_EXISTS("3001","email has not been found"),

    INVALID_REQUEST_TIME_MILLIS("4000","request time millis value is invalid"),
    INVALID_TOKEN_ARRAY_LENGTH("4001","token array length is invalid"),
    INVALID_EMAIL_FORMAT("4002","email format is not invalid"),
    INVALID_TOKEN_HEADER_REQUEST("4003", "token is invalid or empty"),

    EMAIL_SEND_FAILED("5001","email sent failed");

    private final String statusCode;
    private final String message;

    StatusCodeEnum(final String statusCode, final String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
