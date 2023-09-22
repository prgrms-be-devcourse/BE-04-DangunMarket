package com.daangn.dangunmarket.domain.chat.controller;

import com.daangn.dangunmarket.domain.auth.jwt.CustomUser;
import com.daangn.dangunmarket.domain.chat.controller.dto.ChatRoomCreateApiRequest;
import com.daangn.dangunmarket.domain.chat.controller.dto.ChatRoomCreateApiResponse;
import com.daangn.dangunmarket.domain.chat.controller.mapper.ChatDtoApiMapper;
import com.daangn.dangunmarket.domain.chat.facade.ChatFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(
        value = "/chats",
        produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class ChatController {

    private final ChatFacade chatFacade;
    private final ChatDtoApiMapper chatDtoApiMapper;

    public ChatController(ChatFacade chatFacade, ChatDtoApiMapper chatDtoApiMapper) {
        this.chatFacade = chatFacade;
        this.chatDtoApiMapper = chatDtoApiMapper;
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChatRoomCreateApiResponse> createChatRoom(@RequestBody ChatRoomCreateApiRequest request, Authentication authentication) {

        CustomUser customUser = (CustomUser) authentication.getPrincipal();

        Long chatRoomId = chatFacade.createChatRoom(customUser.memberId(), chatDtoApiMapper.toChatRoomCreateRequest(request));
        ChatRoomCreateApiResponse response = chatDtoApiMapper.toChatRoomCreateApiResponse(chatRoomId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/session-info/{sessionId}")
    public ResponseEntity<Void> saveSessionIdWithMemberId(
            @PathVariable String sessionId,
            Authentication authentication
    ){
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        chatFacade.saveSessionInfo(sessionId, customUser.memberId());

        return ResponseEntity.ok().build();
    }

}
