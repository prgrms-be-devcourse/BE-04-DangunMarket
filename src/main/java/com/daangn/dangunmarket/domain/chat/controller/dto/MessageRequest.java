package com.daangn.dangunmarket.domain.chat.controller.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MessageRequest(
        String message,

        @NotNull
        List<String> imageUrls
) {
}
