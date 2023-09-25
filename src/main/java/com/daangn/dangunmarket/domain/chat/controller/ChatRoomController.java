package com.daangn.dangunmarket.domain.chat.controller;

import com.daangn.dangunmarket.domain.auth.jwt.CustomUser;
import com.daangn.dangunmarket.domain.chat.controller.dto.ChatMessagePageApiRequest;
import com.daangn.dangunmarket.domain.chat.controller.dto.ChatMessagePageApiResponses;
import com.daangn.dangunmarket.domain.chat.controller.dto.ChatRoomsFindApiResponses;
import com.daangn.dangunmarket.domain.chat.controller.mapper.ChatDtoApiMapper;
import com.daangn.dangunmarket.domain.chat.facade.ChatRoomFacade;
import com.daangn.dangunmarket.domain.chat.service.ChatRoomService;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatMessagePageRequest;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatMessagePageResponse;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomsFindResponses;
import jakarta.validation.Valid;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;

@RestController
@RequestMapping(value = "/chat-rooms",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatRoomFacade chatRoomFacade;

    public ChatRoomController(ChatRoomService chatRoomService, ChatRoomFacade chatRoomFacade) {
        this.chatRoomService = chatRoomService;
        this.chatRoomFacade = chatRoomFacade;
    }

    @GetMapping("/me")
    public ResponseEntity<ChatRoomsFindApiResponses> findChatRooms(
            Pageable pageable,
            Authentication authentication
    ) {
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        ChatRoomsFindResponses responses = chatRoomService.findChatRoomsByMemberId(customUser.memberId(), pageable);

        ChatRoomsFindApiResponses apiResponses = ChatRoomsFindApiResponses.from(responses);

        return ResponseEntity.ok(apiResponses);
    }

    @DeleteMapping("/{chatRoomId}")
    public ResponseEntity<Void> deleteChatRoom(
            @PathVariable Long chatRoomId,
            Authentication authentication
    ) {
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        chatRoomService.deleteChatRoomByIdAndMemberId(chatRoomId, customUser.memberId());

        return ResponseEntity.noContent().build();
    }

    @EventListener
    public void onDisconnectEvent(SessionDisconnectEvent event) {
        chatRoomService.deleteChatRoomEntryInMemberId(event.getSessionId());
    }
}
