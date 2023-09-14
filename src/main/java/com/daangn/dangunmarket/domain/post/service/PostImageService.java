package com.daangn.dangunmarket.domain.post.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.daangn.dangunmarket.domain.post.model.Post;
import com.daangn.dangunmarket.domain.post.model.PostImage;
import com.daangn.dangunmarket.domain.post.repository.post.PostRepository;
import com.daangn.dangunmarket.global.aws.s3.S3Uploader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Transactional
@Service
public class PostImageService {

    private final PostRepository postRepository;
    private final S3Uploader s3Uploader;

    public PostImageService(PostRepository postRepository, S3Uploader s3Uploader) {
        this.postRepository = postRepository;
        this.s3Uploader = s3Uploader;
    }

    public void editImages(Long postId, List<PostImage> newPostImages, List<MultipartFile> files) {
        Post postToEdit = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("해당 게시글이 존재하지 않습니다."));
        List<PostImage> existingPostImages = postToEdit.getPostImageList();


        List<MultipartFile> addedMultipartFiles = IntStream.range(0, newPostImages.size())
                .filter(i -> existingPostImages.contains(newPostImages.get(i)))
                .mapToObj(files::get)
                .toList();


        List<PostImage> addedImages = newPostImages.stream()
                .filter(image -> !existingPostImages.contains(image))
                .toList();

        addedImages.forEach(postToEdit::addPostImage);
        addedMultipartFiles.forEach(s3Uploader::saveImage);


        List<PostImage> removedImages = existingPostImages.stream()
                .filter(image -> !newPostImages.contains(image))
                .toList();
        removedImages.forEach(image -> {
            postToEdit.removePostImage(image);
            s3Uploader.deleteImage(image.getUrl());
        });

    }


}
