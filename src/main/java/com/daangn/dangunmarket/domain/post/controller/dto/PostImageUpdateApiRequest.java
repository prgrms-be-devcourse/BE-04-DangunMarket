package com.daangn.dangunmarket.domain.post.controller.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record PostImageUpdateApiRequest(
        List<MultipartFile> files,
        List<String> urls) {
}
