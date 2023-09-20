package com.daangn.dangunmarket.domain.chat.exception;

import com.daangn.dangunmarket.global.response.ErrorCode;
import lombok.Getter;

@Getter
public class RoomNotCreateException extends RuntimeException {
    private final ErrorCode errorCode;

    public RoomNotCreateException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
