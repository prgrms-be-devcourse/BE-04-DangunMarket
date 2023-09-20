package com.daangn.dangunmarket.domain.post.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.daangn.dangunmarket.domain.post.model.Post;
import com.daangn.dangunmarket.domain.post.model.PostImage;
import com.daangn.dangunmarket.domain.post.repository.post.PostRepository;
import com.daangn.dangunmarket.global.aws.dto.ImageInfo;
import com.daangn.dangunmarket.global.aws.s3.S3Uploader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Transactional
@Service
public class PostImageService {

    private final PostRepository postRepository;
    private final S3Uploader s3Uploader;

    public PostImageService(PostRepository postRepository, S3Uploader s3Uploader) {
        this.postRepository = postRepository;
        this.s3Uploader = s3Uploader;
    }

    //urls에는 기존 혹은 삭제만 확인한다.
    public void removeImages(Long postId, List<String> urls) {
        Post postToEdit = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("해당 게시글이 존재하지 않습니다."));
        List<PostImage> existingPostImages = postToEdit.getPostImages();

        List<PostImage> postImagesToRemove = existingPostImages.stream().filter(u -> !urls.contains(u.getUrl())).toList();
        postImagesToRemove.forEach(r -> {
            postToEdit.removePostImage(r);
            s3Uploader.deleteImage2(r.getUrl());
        });
    }

    public List<PostImage> saveImagesFromRequest(List<MultipartFile> files) {
        List<ImageInfo> imageInfos = s3Uploader.saveImages(files);
        List<PostImage> postImages = imageInfos.stream()
                .map(p -> new PostImage(p.url(), p.fileName()))
                .toList();

        return postImages;
    }

}
