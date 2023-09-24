package com.daangn.dangunmarket.domain.chat.service;

import com.daangn.dangunmarket.domain.chat.model.ChatRoomInfo;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.ChatRoomInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class ChatRoomInfoService {

    private final ChatRoomInfoRepository chatRoomInfoRepository;

    public ChatRoomInfoService(ChatRoomInfoRepository chatRoomInfoRepository) {
        this.chatRoomInfoRepository = chatRoomInfoRepository;
    }

    public boolean isExistedRoom(Long postId, Long memberId) {
        Optional<ChatRoomInfo> chatRoomInfo = chatRoomInfoRepository.findChatRoomInfoByPostIdAndMemberId(postId, memberId);
        return chatRoomInfo.isPresent();
    }

    public Long findSenderIdByChatRoomInfoAndMyId(Long chatRoomId, Long memberId) {
        return chatRoomInfoRepository.findSenderIdByChatRoomInfoAndMyId(chatRoomId, memberId);
    }

    public Long findPostIdByChatRoomId(Long chatRoomId) {
        return chatRoomInfoRepository.findPostIdByChatRoomId(chatRoomId);
    }

}
