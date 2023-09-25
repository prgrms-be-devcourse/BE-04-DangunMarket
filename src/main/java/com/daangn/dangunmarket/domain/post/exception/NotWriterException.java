package com.daangn.dangunmarket.domain.post.exception;

import com.daangn.dangunmarket.global.response.ErrorCode;
import lombok.Getter;

@Getter
public class NotWriterException extends RuntimeException {
    private final ErrorCode errorCode;

    public NotWriterException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    
}
