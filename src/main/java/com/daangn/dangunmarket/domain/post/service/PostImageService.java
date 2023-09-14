package com.daangn.dangunmarket.domain.post.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.daangn.dangunmarket.domain.post.model.Post;
import com.daangn.dangunmarket.domain.post.model.PostImage;
import com.daangn.dangunmarket.domain.post.repository.post.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class PostImageService {

    private final PostRepository postRepository;

    public PostImageService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PostImage> editImages(Long postId, List<PostImage> newPostImages) {
        Post postToEdit = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("해당 게시글이 존재하지 않습니다."));
        List<PostImage> existingPostImages = postToEdit.getPostImageList();

        List<PostImage> addedImages = newPostImages.stream()
                .filter(image -> !existingPostImages.contains(image))
                .toList();

        List<PostImage> removedImages = existingPostImages.stream()
                .filter(image -> !newPostImages.contains(image))
                .toList();

        addedImages.forEach(postToEdit::addPostImage);
        removedImages.forEach(postToEdit::removePostImage);

        return removedImages;
    }

}
