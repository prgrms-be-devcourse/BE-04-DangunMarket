package com.daangn.dangunmarket.domain.chat.service;

import com.daangn.dangunmarket.global.aws.dto.ImageInfo;
import com.daangn.dangunmarket.global.aws.s3.S3Uploader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ChatService {

    private final S3Uploader s3Uploader;

    public ChatService(S3Uploader s3Uploader) {
        this.s3Uploader = s3Uploader;
    }

    public List<ImageInfo> uploadImages(List<MultipartFile> files) {
        return s3Uploader.saveImages(files);
    }

}
