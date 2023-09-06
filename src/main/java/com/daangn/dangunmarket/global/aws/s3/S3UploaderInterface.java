package com.daangn.dangunmarket.global.aws.s3;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3UploaderInterface {

    List<String> saveImages(List<MultipartFile> multipartFiles);
    String saveImage(MultipartFile multipartFile);

}
