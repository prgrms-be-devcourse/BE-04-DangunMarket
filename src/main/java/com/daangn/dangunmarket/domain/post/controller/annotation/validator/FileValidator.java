package com.daangn.dangunmarket.domain.post.controller.annotation.validator;

import com.daangn.dangunmarket.domain.post.controller.annotation.FileSize;
import com.daangn.dangunmarket.domain.post.controller.dto.PostImageUpdateApiRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileValidator implements ConstraintValidator<FileSize, PostImageUpdateApiRequest> {

    private static final int MAX_IMAGE_FILE_COUNT = 3;

    @Override
    public boolean isValid(PostImageUpdateApiRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return true;
        }

        int totalFileCount = fileCount(request) + urlCount(request);

        if (totalFileCount > MAX_IMAGE_FILE_COUNT) {
            context.buildConstraintViolationWithTemplate("사진의 개수가 너무 많습니다.").addPropertyNode("files");
            return false;
        }

        return true;
    }

    private int urlCount(PostImageUpdateApiRequest request) {
        if (request.urls() == null) {
            return 0;
        }

        return request.urls().size();
    }

    private int fileCount(PostImageUpdateApiRequest request) {
        if (request.files() == null) {
            return 0;
        }

        return request.files().size();
    }

}
