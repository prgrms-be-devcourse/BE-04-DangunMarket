package com.daangn.dangunmarket.global.response;

import lombok.Getter;

@Getter
public enum ErrorCode {

    //global
    INTERNAL_SERVER_ERROR("G001", "Internal Server Error"),

    //s3 - image
    FAIL_TO_UPLOAD_IMAGES("S001", "이미지를 업로드 할 수 없습니다."),

    //area
    NOT_FOUND_AREA_ENTITY("A001", "Area를 찾을 수 없습니다."),

    //post
    NOT_FOUND_CATEGORY_ENTITY("P001", "Category를 찾을 수 없습니다."),
    NOT_FOUND_POST_ENTITY("P002", "Post를 찾을 수 없습니다."),
    NOT_FOUND_POST_LIKE_ENTITY("P003", "Post의 좋아요 기록을 찾을 수 없습니다."),
    ALREADY_EXISTS_POST_LIKE("P004", "Post의 좋아요 기록이 이미 존재합니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
