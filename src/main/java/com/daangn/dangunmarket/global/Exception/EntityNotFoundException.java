package com.daangn.dangunmarket.global.Exception;

import com.daangn.dangunmarket.global.response.ErrorCode;

public class EntityNotFoundException extends RuntimeException{
    private final ErrorCode errorCode;

    public EntityNotFoundException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
