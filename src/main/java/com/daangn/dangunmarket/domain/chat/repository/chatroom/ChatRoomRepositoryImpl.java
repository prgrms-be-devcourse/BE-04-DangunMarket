package com.daangn.dangunmarket.domain.chat.repository.chatroom;

import org.springframework.stereotype.Repository;

@Repository
public class ChatRoomRepositoryImpl implements ChatRoomRepository{

    private final ChatRoomJpaRepository chatRoomJpaRepository;

    public ChatRoomRepositoryImpl(ChatRoomJpaRepository chatRoomJpaRepository) {
        this.chatRoomJpaRepository = chatRoomJpaRepository;
    }

}
