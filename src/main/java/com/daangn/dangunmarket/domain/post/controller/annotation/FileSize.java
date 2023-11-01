package com.daangn.dangunmarket.domain.post.controller.annotation;

import com.daangn.dangunmarket.domain.post.controller.annotation.validator.FileValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileValidator.class)
public @interface FileSize {
    String message() default "사진 3장 초과";

    Class[] groups() default {};

    Class[] payload() default {};
}
