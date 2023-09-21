package com.daangn.dangunmarket.domain.chat.service;

import com.daangn.dangunmarket.domain.chat.model.ChatRoom;
import com.daangn.dangunmarket.domain.chat.model.ChatRoomInfo;
import com.daangn.dangunmarket.domain.chat.repository.chatroom.ChatRoomRepository;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.ChatRoomInfoRepository;
import com.daangn.dangunmarket.domain.chat.service.dto.ChatRoomCreateRequest;
import com.daangn.dangunmarket.domain.post.repository.post.PostRepository;
import com.daangn.dangunmarket.global.exception.EntityNotFoundException;
import com.daangn.dangunmarket.global.response.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChatRoomService {

    private final ChatRoomInfoRepository chatRoomInfoRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomService(ChatRoomInfoRepository chatRoomInfoRepository, ChatRoomRepository chatRoomRepository) {
        this.chatRoomInfoRepository = chatRoomInfoRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    @Transactional
    public Long createChatRoom(Long memberId, Long writerId, ChatRoomCreateRequest request) {

        ChatRoom chatRoom = new ChatRoom();
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        ChatRoomInfo sellerChatRoomInfo = new ChatRoomInfo(true, request.postId(), savedChatRoom, writerId);
        chatRoomInfoRepository.save(sellerChatRoomInfo);
        ChatRoomInfo buyerChatRoomInfo = new ChatRoomInfo(false, request.postId(), savedChatRoom, memberId);
        chatRoomInfoRepository.save(buyerChatRoomInfo);

        return chatRoom.getId();
    }

    @Transactional
    public void deleteChatRoomById(Long chatRoomId, Long memberId) {
        List<ChatRoomInfo> findChatRoomInfos = chatRoomInfoRepository.findByChatRoomId(chatRoomId);

        ChatRoomInfo chatRoomInfo = findChatRoomInfos.stream()
                .filter(e -> e.isSameMember(memberId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_ENTITY));

        chatRoomInfo.deleteChatRoomInfo();

        if (findChatRoomInfos.size() < 2){
            chatRoomInfo.getChatRoom().deleteChatRoom();
        }
    }

}
