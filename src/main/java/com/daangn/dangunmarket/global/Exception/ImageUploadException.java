package com.daangn.dangunmarket.global.Exception;

import com.daangn.dangunmarket.global.response.ErrorCode;

public class ImageUploadException extends RuntimeException {
    private final ErrorCode errorCode;

    public ImageUploadException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
