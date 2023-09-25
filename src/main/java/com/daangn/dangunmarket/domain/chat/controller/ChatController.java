package com.daangn.dangunmarket.domain.chat.controller;

import com.daangn.dangunmarket.domain.auth.jwt.CustomUser;
import com.daangn.dangunmarket.domain.chat.controller.dto.ChatRoomCheckInApiResponse;
import com.daangn.dangunmarket.domain.chat.controller.dto.ChatRoomCreateApiRequest;
import com.daangn.dangunmarket.domain.chat.controller.dto.ChatRoomCreateApiResponse;
import com.daangn.dangunmarket.domain.chat.controller.dto.SessionInfoSaveApiRequest;
import com.daangn.dangunmarket.domain.chat.controller.mapper.ChatDtoApiMapper;
import com.daangn.dangunmarket.domain.chat.facade.ChatRoomFacade;
import com.daangn.dangunmarket.domain.chat.facade.dto.ChatRoomCheckInParamResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequestMapping(
        value = "/chats",
        produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class ChatController {

    private final ChatRoomFacade chatRoomFacade;
    private final ChatDtoApiMapper chatDtoApiMapper;

    public ChatController(ChatRoomFacade createChatRoom, ChatDtoApiMapper chatDtoApiMapper) {
        this.chatRoomFacade = createChatRoom;
        this.chatDtoApiMapper = chatDtoApiMapper;
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChatRoomCreateApiResponse> createChatRoom(@RequestBody ChatRoomCreateApiRequest request, Authentication authentication) {

        CustomUser customUser = (CustomUser) authentication.getPrincipal();

        Long chatRoomId = chatRoomFacade.createChatRoom(chatDtoApiMapper.toChatRoomCreateRequest(request, customUser.memberId()));
        ChatRoomCreateApiResponse response = chatDtoApiMapper.toChatRoomCreateApiResponse(chatRoomId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(
            value = "/{roomId}",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChatRoomCheckInApiResponse> checkInChatRoom(@PathVariable Long roomId, Authentication authentication) {

        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        ChatRoomCheckInParamResponse chatRoomCheckInParamResponse = chatRoomFacade.checkInChatRoom(roomId, customUser.memberId());
        ChatRoomCheckInApiResponse chatRoomCheckInApiResponse = chatDtoApiMapper.toChatRoomCheckInApiResponse(chatRoomCheckInParamResponse);

        URI uri = createURI(chatRoomCheckInApiResponse.postId());

        return ResponseEntity.ok().header("Content-Location", uri.toString()).body(chatRoomCheckInApiResponse);
    }

    private static URI createURI(Long postId) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(postId)
                .toUri();
    }

    @PutMapping("/session-info/{sessionId}")
    public ResponseEntity<Void> saveSessionInfo(
            @PathVariable String sessionId,
            @RequestBody @Valid SessionInfoSaveApiRequest request,
            Authentication authentication
    ){
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        chatRoomFacade.saveSessionInfo(chatDtoApiMapper.toSessionInfoSaveParamRequest(
                sessionId,
                request.roomId(),
                customUser.memberId())
        );

        return ResponseEntity.ok().build();
    }

}
