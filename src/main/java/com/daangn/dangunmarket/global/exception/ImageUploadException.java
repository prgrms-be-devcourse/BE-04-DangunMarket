package com.daangn.dangunmarket.global.exception;

import com.daangn.dangunmarket.global.response.ErrorCode;
import lombok.Getter;

@Getter
public class ImageUploadException extends RuntimeException {
    private final ErrorCode errorCode;

    public ImageUploadException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
