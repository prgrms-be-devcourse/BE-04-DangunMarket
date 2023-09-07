package com.daangn.dangunmarket.global.response;

import lombok.Getter;

@Getter
public enum ErrorCode {

    //global
    INTERNAL_SERVER_ERROR("G001", "Internal Server Error"),

    //s3 - image
    FAIL_TO_UPLOAD_IMAGES("S001", "이미지를 업로드 할 수 없습니다."),

    //member
    NOT_FOUND_MEMBER_ENTITY("M001", "멤버를 찾을 수 없습니다."),

    //area
    NOT_FOUND_AREA_ENTITY("A001", "Area를 찾을 수 없습니다."),

    //product
    NOT_FOUND_PRODUCT_ENTITY("P001", "Product를 찾을 수 없습니다."),
    NOT_FOUND_CATEGORY_ENTITY("C001", "Category를 찾을 수 없습니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
