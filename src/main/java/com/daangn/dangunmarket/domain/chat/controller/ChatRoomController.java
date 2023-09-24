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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/chats",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatDtoApiMapper chatDtoApiMapper;
    private final ChatRoomFacade chatRoomFacade;

    public ChatRoomController(ChatRoomService chatRoomService, ChatDtoApiMapper chatDtoApiMapper, ChatRoomFacade chatRoomFacade) {
        this.chatRoomService = chatRoomService;
        this.chatDtoApiMapper = chatDtoApiMapper;
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

    @GetMapping("/messages")
    public ResponseEntity<ChatMessagePageApiResponses> getChatMessages(
            @ModelAttribute @Valid ChatMessagePageApiRequest chatMessagePageApiRequest,
            Authentication authentication
    ) {
        CustomUser customUser = (CustomUser) authentication.getPrincipal();

        ChatMessagePageRequest chatMessagePageRequest = chatDtoApiMapper.toChatMessagePageRequest(chatMessagePageApiRequest);
        List<ChatMessagePageResponse> chatRoomIdWithPagination = chatRoomService.findByChatRoomIdWithPagination(chatMessagePageRequest, customUser.memberId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(chatDtoApiMapper.toChatMessagePageApiResponses(chatRoomIdWithPagination));
    }
}
