package com.daangn.dangunmarket.domain.chat.controller;

import com.daangn.dangunmarket.domain.auth.jwt.CustomUser;
import com.daangn.dangunmarket.domain.chat.controller.dro.ChatRoomsFindApiResponses;
import com.daangn.dangunmarket.domain.chat.service.ChatRoomService;
import com.daangn.dangunmarket.domain.chat.service.ChatService;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomsFindResponses;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/chats",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class ChatRoomController {

    private final ChatService chatService;
    private final ChatRoomService chatRoomService;

    public ChatRoomController(ChatService chatService, ChatRoomService chatRoomService) {
        this.chatService = chatService;
        this.chatRoomService = chatRoomService;
    }

    @GetMapping("/me")
    public ResponseEntity<ChatRoomsFindApiResponses> findChatRooms(
            Pageable pageable,
            Authentication authentication
    ) {
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        ChatRoomsFindResponses responses = chatService.findChatRoomsByMemberId(customUser.memberId(), pageable);

        ChatRoomsFindApiResponses apiResponses = ChatRoomsFindApiResponses.from(responses);

        return ResponseEntity.ok(apiResponses);
    }

    @DeleteMapping("/{chatRoomId}")
    public ResponseEntity<Void> deleteChatRoom(
            @PathVariable Long chatRoomId,
            Authentication authentication
    ){
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        chatRoomService.deleteChatRoomById(chatRoomId, customUser.memberId());

        return ResponseEntity.noContent().build();
    }
}
