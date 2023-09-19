package com.daangn.dangunmarket.global.aws.s3;

import com.daangn.dangunmarket.global.aws.dto.ImageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3Uploader {

    List<ImageInfo> saveImages(List<MultipartFile> multipartFiles);

    ImageInfo saveImage(MultipartFile multipartFile);

    void deleteImage(String fileName);

}
