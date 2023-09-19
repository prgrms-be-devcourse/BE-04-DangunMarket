package com.daangn.dangunmarket.domain.chat.repository.chatroominfo;

import com.daangn.dangunmarket.domain.chat.model.ChatRoomInfo;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ChatRoomInfoRepositoryImpl implements ChatRoomInfoRepository{

    private final ChatRoomInfoJpaRepository chatRoomInfoJpaRepository;

    public ChatRoomInfoRepositoryImpl(ChatRoomInfoJpaRepository chatRoomInfoJpaRepository) {
        this.chatRoomInfoJpaRepository = chatRoomInfoJpaRepository;
    }

    @Override
    public Optional<ChatRoomInfo> findChatRoomInfoByPostIdAndMemberId(Long postId, Long memberId) {
        return chatRoomInfoJpaRepository.findChatRoomInfoByPostIdAndMemberId(postId,memberId);
    }

    @Override
    public ChatRoomInfo save(ChatRoomInfo chatRoomInfo) {
        return chatRoomInfoJpaRepository.save(chatRoomInfo);
    }

}
