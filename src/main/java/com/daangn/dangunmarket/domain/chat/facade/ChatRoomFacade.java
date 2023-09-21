package com.daangn.dangunmarket.domain.chat.facade;

import com.daangn.dangunmarket.domain.chat.exception.RoomNotCreateException;
import com.daangn.dangunmarket.domain.chat.service.ChatRoomInfoService;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomCreateRequest;
import com.daangn.dangunmarket.domain.chat.service.ChatRoomService;
import com.daangn.dangunmarket.domain.post.service.PostService;
import com.daangn.dangunmarket.domain.post.service.dto.PostFindResponse;
import org.springframework.stereotype.Service;

import static com.daangn.dangunmarket.global.response.ErrorCode.NOT_CREATE_CHAT_ROOM;

@Service
public class ChatRoomFacade {

    private final PostService postService;
    private final ChatRoomService chatRoomService;
    private final ChatRoomInfoService chatRoomInfoService;

    public ChatRoomFacade(PostService postService, ChatRoomService chatRoomService, ChatRoomInfoService chatRoomInfoService) {
        this.postService = postService;
        this.chatRoomService = chatRoomService;
        this.chatRoomInfoService = chatRoomInfoService;
    }

    public Long createChatRoom(Long memberId, ChatRoomCreateRequest request){

        if(chatRoomInfoService.isExistedRoom(request.postId(),memberId)) {
            throw new RoomNotCreateException(NOT_CREATE_CHAT_ROOM);
        }

        PostFindResponse postFindResponse = postService.findById(request.postId());
        return chatRoomService.createChatRoom(memberId,postFindResponse.memberId(),request);
    }

}