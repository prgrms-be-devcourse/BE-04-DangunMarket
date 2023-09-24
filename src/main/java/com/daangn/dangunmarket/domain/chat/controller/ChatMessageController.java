package com.daangn.dangunmarket.domain.chat.controller;

import com.daangn.dangunmarket.domain.auth.jwt.CustomUser;
import com.daangn.dangunmarket.domain.chat.controller.dto.ChatImageUploadApiResponse;
import com.daangn.dangunmarket.domain.chat.controller.dto.MessageRequest;
import com.daangn.dangunmarket.domain.chat.service.ChatService;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatMessageResponse;
import com.daangn.dangunmarket.global.aws.dto.ImageInfo;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ChatMessageController {

    private static final String DESTINATION_URL = "/sub/chat/room/";
    private final ChatService chatService;
    private final SimpMessageSendingOperations sendingOperations;

    public ChatMessageController(ChatService chatService, SimpMessageSendingOperations sendingOperations) {
        this.chatService = chatService;
        this.sendingOperations = sendingOperations;
    }

    /**
     * 채팅 이미지 저장 API
     */
    @PostMapping(path = "/chats/images", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
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

    /**
     * 채팅 발송
     */
    @MessageMapping("/chats/{roomId}/messages")
    public void saveMessage(@DestinationVariable("roomId") Long roomId,
                            Authentication authentication,
                            @Payload @Valid MessageRequest messageRequest) {

        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        ChatMessageResponse response = chatService.saveMessage(customUser.memberId(), roomId, messageRequest);
        sendingOperations.convertAndSend(DESTINATION_URL + roomId, response);
    }

}
