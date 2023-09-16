package com.daangn.dangunmarket.global.exception;

import com.daangn.dangunmarket.global.response.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidPostLikeException extends RuntimeException {
    private final ErrorCode errorCode;

    public InvalidPostLikeException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
