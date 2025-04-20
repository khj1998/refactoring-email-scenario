package com.email.exception;

import com.email.common.StatusCodeEnum;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final String statusCode;
    private final String message;

    public BaseException(StatusCodeEnum statusCodeEnum) {
        super(statusCodeEnum.getMessage());
        this.statusCode = statusCodeEnum.getStatusCode();
        this.message = statusCodeEnum.getMessage();
    }
}
