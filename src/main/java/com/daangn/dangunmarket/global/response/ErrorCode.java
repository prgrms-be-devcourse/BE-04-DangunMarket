package com.daangn.dangunmarket.global.response;

import lombok.Getter;

@Getter
public enum ErrorCode {

    //global
    INTERNAL_SERVER_ERROR("G001", "Internal Server Error"),
    NOT_FOUND_ENTITY("G002", "엔티티를 찾을 수 없습니다."),

    //s3 - image
    FAIL_TO_UPLOAD_IMAGES("S001", "이미지를 업로드 할 수 없습니다."),

    //member
    NOT_FOUND_MEMBER_ENTITY("M001", "멤버를 찾을 수 없습니다."),

    //area
    NOT_FOUND_AREA_ENTITY("A001", "Area를 찾을 수 없습니다."),

    //login
    EXPIRED_TOKEN("L001","토큰이 만료되었씁니다."),
    UNAUTHORIZED_TOKEN("L002","인증되지 않은 토큰입니다."),
    OAUTH_CLIENT_SERVER_ERROR("L003","oauth 클라이언트 서버 에러입니다."),

    //product
    NOT_FOUND_POST_ENTITY("P001", "Post를 찾을 수 없습니다."),
    NOT_FOUND_CATEGORY_ENTITY("C001", "Category를 찾을 수 없습니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
