package com.daangn.dangunmarket.domain.post.exception;

import com.daangn.dangunmarket.global.response.ErrorCode;

public class UnauthorizedAccessException extends RuntimeException {

    private final ErrorCode errorCode;

    public UnauthorizedAccessException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
