package com.daangn.dangunmarket.global.response;

import lombok.Getter;

@Getter
public enum ErrorCode {

    //global
    INTERNAL_SERVER_ERROR("G001", "Internal Server Error"),

    //s3 - image
    FAIL_TO_UPLOAD_IMAGES("S001", "이미지를 업로드 할 수 없습니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
