package com.daangn.dangunmarket.domain.chat.repository.chatroom;

import com.daangn.dangunmarket.domain.chat.model.ChatRoom;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ChatRoomRepositoryImpl implements ChatRoomRepository{

    private final ChatRoomJpaRepository chatRoomJpaRepository;

    public ChatRoomRepositoryImpl(ChatRoomJpaRepository chatRoomJpaRepository) {
        this.chatRoomJpaRepository = chatRoomJpaRepository;
    }

    @Override
    public ChatRoom save(ChatRoom chatRoom) {
        return chatRoomJpaRepository.save(chatRoom);
    }

    @Override
    public Optional<ChatRoom> findById(Long chatRoomId) {
        return chatRoomJpaRepository.findById(chatRoomId);
    }

}
