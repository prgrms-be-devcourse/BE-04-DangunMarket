package com.daangn.dangunmarket.domain.auth.exception;

import com.daangn.dangunmarket.global.response.ErrorCode;

public class TokenValidFailedException extends IllegalStateException {

    private final ErrorCode errorCode;

    public TokenValidFailedException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
