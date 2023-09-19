package com.daangn.dangunmarket.domain.post.controller.annotation.validator;

import com.daangn.dangunmarket.domain.post.controller.annotation.FileSize;
import com.daangn.dangunmarket.domain.post.controller.dto.PostImageUpdateApiRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileValidator implements ConstraintValidator<FileSize, PostImageUpdateApiRequest> {

    @Override
    public boolean isValid(PostImageUpdateApiRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return true;
        }

        int totalFileCount = (request.files() != null ? request.files().size() : 0) +
                (request.urls() != null ? request.urls().size() : 0);

        return totalFileCount < 4;
    }
}
