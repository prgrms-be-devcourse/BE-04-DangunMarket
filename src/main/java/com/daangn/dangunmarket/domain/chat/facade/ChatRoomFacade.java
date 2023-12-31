package com.daangn.dangunmarket.domain.chat.facade;

import com.daangn.dangunmarket.domain.chat.facade.dto.ChatRoomCheckInParamResponse;
import com.daangn.dangunmarket.domain.chat.facade.dto.SessionInfoSaveParamRequest;
import com.daangn.dangunmarket.domain.chat.facade.mapper.ChatRoomParamDtoMapper;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomCreateRequest;
import com.daangn.dangunmarket.domain.chat.service.ChatRoomService;
import com.daangn.dangunmarket.domain.chat.service.ChatService;
import com.daangn.dangunmarket.domain.member.service.MemberService;
import com.daangn.dangunmarket.domain.post.service.PostService;
import com.daangn.dangunmarket.domain.post.service.dto.PostFindResponse;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomFacade {

    private final PostService postService;
    private final ChatRoomService chatRoomService;
    private final MemberService memberService;
    private final ChatRoomParamDtoMapper chatRoomParamDtoMapper;
    private final ChatService chatService;

    public ChatRoomFacade(PostService postService, ChatRoomService chatRoomService,
                          MemberService memberService, ChatRoomParamDtoMapper chatRoomParamDtoMapper, ChatService chatService) {

        this.postService = postService;
        this.chatRoomService = chatRoomService;
        this.memberService = memberService;
        this.chatRoomParamDtoMapper = chatRoomParamDtoMapper;
        this.chatService = chatService;
    }

    public Long createChatRoom(ChatRoomCreateRequest request){
        chatRoomService.isExistedChatRoomByBuyer(request.postId(), request.memberId());

        PostFindResponse postFindResponse = postService.findById(request.postId());
        return chatRoomService.createChatRoom(postFindResponse.memberId(), request);
    }

    public ChatRoomCheckInParamResponse enterChatRoom(Long chatRoomId, Long memberId) {
        chatRoomService.addMemberToRoom(chatRoomId.toString(),memberId.toString());
        chatRoomService.readAllMessage(chatRoomId, memberId);

        return chatRoomParamDtoMapper.toChatRoomCheckInParamResponse(chatRoomService.findPostWithMember(chatRoomId));
    }

    public void saveSessionInfo(SessionInfoSaveParamRequest request){
        chatService.saveSessionInfo(chatRoomParamDtoMapper.toSessionInfoSaveRequest(request));
    }

}
