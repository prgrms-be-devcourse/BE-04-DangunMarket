package com.daangn.dangunmarket.domain.chat.facade;

import com.daangn.dangunmarket.domain.chat.exception.RoomNotCreateException;
import com.daangn.dangunmarket.domain.chat.facade.dto.ChatRoomCheckInParamResponse;
import com.daangn.dangunmarket.domain.chat.facade.mapper.ChatRoomParamDtoMapper;
import com.daangn.dangunmarket.domain.chat.service.ChatRoomInfoService;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomCreateRequest;
import com.daangn.dangunmarket.domain.chat.service.ChatRoomService;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatWithPostAndMemberResponse;
import com.daangn.dangunmarket.domain.member.service.MemberService;
import com.daangn.dangunmarket.domain.member.service.dto.MemberFindResponse;
import com.daangn.dangunmarket.domain.post.service.PostService;
import com.daangn.dangunmarket.domain.post.service.dto.PostFindResponse;
import org.springframework.stereotype.Service;

import static com.daangn.dangunmarket.global.response.ErrorCode.NOT_CREATE_CHAT_ROOM;

@Service
public class ChatRoomFacade {

    private final PostService postService;
    private final ChatRoomService chatRoomService;
    private final ChatRoomInfoService chatRoomInfoService;
    private final MemberService memberService;
    private final ChatRoomParamDtoMapper chatRoomParamDtoMapper;

    public ChatRoomFacade(PostService postService, ChatRoomService chatRoomService, ChatRoomInfoService chatRoomInfoService,
                          MemberService memberService, ChatRoomParamDtoMapper chatRoomParamDtoMapper) {
        this.postService = postService;
        this.chatRoomService = chatRoomService;
        this.chatRoomInfoService = chatRoomInfoService;
        this.memberService = memberService;
        this.chatRoomParamDtoMapper = chatRoomParamDtoMapper;
    }

    public Long createChatRoom(ChatRoomCreateRequest request){

        if(chatRoomInfoService.isExistedRoom(request.postId(),request.memberId())) {
            throw new RoomNotCreateException(NOT_CREATE_CHAT_ROOM);
        }

        PostFindResponse postFindResponse = postService.findById(request.postId());
        return chatRoomService.createChatRoom(postFindResponse.memberId(),request);
    }

    public ChatRoomCheckInParamResponse checkInChatRoom(Long chatRoomId, Long memberId) {
        chatRoomService.addMemberToRoom(chatRoomId.toString(),memberId.toString());
        chatRoomService.readAllMessage(chatRoomId, memberId);

        return chatRoomParamDtoMapper.toChatRoomCheckInParamResponse(chatRoomService.findPostWithMember(chatRoomId));
    }

}
