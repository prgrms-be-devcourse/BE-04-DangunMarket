package com.daangn.dangunmarket.domain.chat.facade;

import com.daangn.dangunmarket.domain.chat.exception.RoomNotCreateException;
import com.daangn.dangunmarket.domain.chat.service.ChatRoomInfoService;
import com.daangn.dangunmarket.domain.chat.service.ChatService;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomCreateRequest;
import com.daangn.dangunmarket.domain.chat.service.ChatRoomService;
import com.daangn.dangunmarket.domain.member.service.MemberService;
import com.daangn.dangunmarket.domain.post.service.PostService;
import com.daangn.dangunmarket.domain.post.service.dto.PostFindResponse;
import org.springframework.stereotype.Service;

import static com.daangn.dangunmarket.global.response.ErrorCode.NOT_CREATE_CHAT_ROOM;

@Service
public class ChatFacade {

    private final PostService postService;
    private final ChatRoomService chatRoomService;
    private final ChatRoomInfoService chatRoomInfoService;
    private final ChatService chatService;
    private final MemberService memberService;

    public ChatFacade(PostService postService, ChatRoomService chatRoomService, ChatRoomInfoService chatRoomInfoService, ChatService chatService, MemberService memberService) {
        this.postService = postService;
        this.chatRoomService = chatRoomService;
        this.chatRoomInfoService = chatRoomInfoService;
        this.chatService = chatService;
        this.memberService = memberService;
    }

    public Long createChatRoom(Long memberId, ChatRoomCreateRequest request){

        if(chatRoomInfoService.isExistedRoom(request.postId(),memberId)) {
            throw new RoomNotCreateException(NOT_CREATE_CHAT_ROOM);
        }

        PostFindResponse postFindResponse = postService.findById(request.postId());
        return chatRoomService.createChatRoom(memberId,postFindResponse.memberId(),request);
    }

    public void saveSessionInfo(String sessionId, Long memberId){
        memberService.findById(memberId);

        chatService.saveSessionInfo(sessionId, memberId);
    }

}
