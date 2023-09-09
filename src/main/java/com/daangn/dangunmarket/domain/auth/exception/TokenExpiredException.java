package com.daangn.dangunmarket.domain.auth.exception;

import com.daangn.dangunmarket.global.response.ErrorCode;

public class TokenExpiredException extends RuntimeException {

    private final ErrorCode errorCode;

    public TokenExpiredException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
