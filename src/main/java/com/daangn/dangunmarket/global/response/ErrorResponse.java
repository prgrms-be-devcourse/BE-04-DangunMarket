package com.daangn.dangunmarket.global.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Global Exception Rest Handler에서 발생한 에러에 대한 응답 처리를 관리
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
    private String code; //서버 내 에러코드
    private String message; //에러 메시지
    private List<FieldError> fieldErrors; //상세 에러 메시지
    private String reason; //에러 이유 - Exception 정보

    public static ErrorResponse of(final ErrorCode errorCode, final BindingResult bindingResult) {
        return new ErrorResponse(errorCode, FieldError.of(bindingResult));
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode);
    }

    public static ErrorResponse of(final ErrorCode errorCode, final String reason) {
        return new ErrorResponse(errorCode, reason);
    }

    private ErrorResponse(final ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.fieldErrors = new ArrayList<>();
    }

    private ErrorResponse(final ErrorCode errorCode, final String reason) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.reason = reason;
    }

    private ErrorResponse(final ErrorCode errorCode, final List<FieldError> fieldErrors) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.fieldErrors = fieldErrors;
    }

    public static class FieldError {
        private final String field;
        private final String value;
        private final String reason;

        private static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }

        private FieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }
    }
}
