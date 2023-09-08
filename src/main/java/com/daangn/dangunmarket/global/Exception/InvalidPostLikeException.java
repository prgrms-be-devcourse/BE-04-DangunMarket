package com.daangn.dangunmarket.global.Exception;

import com.daangn.dangunmarket.global.response.ErrorCode;

public class InvalidPostLikeException extends RuntimeException {
    private final ErrorCode errorCode;

    public InvalidPostLikeException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
