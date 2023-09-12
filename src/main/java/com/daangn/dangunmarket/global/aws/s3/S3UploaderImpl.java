package com.daangn.dangunmarket.global.aws.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.daangn.dangunmarket.global.exception.ImageUploadException;
import com.daangn.dangunmarket.global.response.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3UploaderImpl implements S3Uploader{

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;

    @Transactional
    @Override
    public List<String> saveImages(List<MultipartFile> multipartFiles) {
        List<String> resultList = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            String value = saveImage(multipartFile);
            resultList.add(value);
        }

        return resultList;
    }

    @Transactional
    @Override
    public String saveImage(MultipartFile multipartFile) {
        String originalFileName = multipartFile.getOriginalFilename();
        String changedFileName = changeFileName(originalFileName);
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());
            objectMetadata.setContentLength(multipartFile.getInputStream().available());

            amazonS3Client.putObject(bucket, changedFileName, multipartFile.getInputStream(), objectMetadata);

            return amazonS3Client.getUrl(bucket, changedFileName).toString();
        } catch (IOException e) {
            throw new ImageUploadException(ErrorCode.FAIL_TO_UPLOAD_IMAGES);
        } catch (AmazonServiceException e) {
            log.error("uploadToAWS AmazonServiceException filePath={}, error={}", changedFileName, e.getMessage());
            throw new ImageUploadException(ErrorCode.INTERNAL_SERVER_ERROR);
        } catch (SdkClientException e) {
            log.error("uploadToAWS SdkClientException filePath={}, error = {}", changedFileName, e.getMessage());
            throw new ImageUploadException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private String changeFileName(String fileName){
        String uuid = UUID.randomUUID().toString();
        return uuid + "-" + fileName;
    }

}
