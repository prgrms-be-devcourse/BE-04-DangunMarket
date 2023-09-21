package com.daangn.dangunmarket.domain.chat.controller;

import com.daangn.dangunmarket.domain.chat.controller.dto.ChatImageUploadApiResponse;
import com.daangn.dangunmarket.domain.chat.service.ChatService;
import com.daangn.dangunmarket.global.aws.dto.ImageInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/chat/messages")
public class ChatMessageController {

    private final ChatService chatService;

    public ChatMessageController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * 채팅 이미지 저장 API
     */
    @PostMapping(path = "/images", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ChatImageUploadApiResponse> uploadChatImages(
            @RequestPart List<MultipartFile> files,
            Authentication authentication
    ) {
        List<ImageInfo> imageInfos = chatService.uploadImages(files);
        ChatImageUploadApiResponse response = ChatImageUploadApiResponse.from(imageInfos);

        return ResponseEntity
                .status((HttpStatus.OK))
                .body(response);
    }

}
