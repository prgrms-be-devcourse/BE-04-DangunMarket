package com.daangn.dangunmarket.domain.chat.controller;

import com.daangn.dangunmarket.domain.auth.jwt.CustomUser;
import com.daangn.dangunmarket.domain.chat.controller.dto.ChatImageUploadApiResponse;
import com.daangn.dangunmarket.domain.chat.controller.dto.ChatMessagePageApiRequest;
import com.daangn.dangunmarket.domain.chat.controller.dto.ChatMessagePageApiResponses;
import com.daangn.dangunmarket.domain.chat.controller.dto.MessageRequest;
import com.daangn.dangunmarket.domain.chat.controller.mapper.ChatDtoApiMapper;
import com.daangn.dangunmarket.domain.chat.service.ChatRoomService;
import com.daangn.dangunmarket.domain.chat.service.ChatService;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatMessagePageRequest;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatMessagePageResponse;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ChatMessageController {

    private static final String DESTINATION_URL = "/sub/chat/room/";
    private final ChatService chatService;
    private final SimpMessageSendingOperations sendingOperations;
    private final ChatDtoApiMapper chatDtoApiMapper;
    private final ChatRoomService chatRoomService;

    public ChatMessageController(ChatService chatService, SimpMessageSendingOperations sendingOperations, ChatDtoApiMapper chatDtoApiMapper, ChatRoomService chatRoomService) {
        this.chatService = chatService;
        this.sendingOperations = sendingOperations;
        this.chatDtoApiMapper = chatDtoApiMapper;
        this.chatRoomService = chatRoomService;
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

    /**
     * 채팅 메세지 목록 생성
     */
    @GetMapping("/chats/{roomId}/messages")
    public ResponseEntity<ChatMessagePageApiResponses> getChatMessages(
            @PathVariable Long roomId,
            @ModelAttribute @Valid ChatMessagePageApiRequest chatMessagePageApiRequest,
            Authentication authentication
    ) {
        CustomUser customUser = (CustomUser) authentication.getPrincipal();

        ChatMessagePageRequest chatMessagePageRequest = chatDtoApiMapper.toChatMessagePageRequest(chatMessagePageApiRequest,roomId);
        List<ChatMessagePageResponse> chatRoomIdWithPagination = chatRoomService.findByChatRoomIdWithPagination(chatMessagePageRequest, customUser.memberId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(chatDtoApiMapper.toChatMessagePageApiResponses(chatRoomIdWithPagination));
    }

}
