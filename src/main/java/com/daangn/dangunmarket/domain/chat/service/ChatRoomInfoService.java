package com.daangn.dangunmarket.domain.chat.service;

import com.daangn.dangunmarket.domain.chat.model.ChatRoomInfo;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.ChatRoomInfoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

}
