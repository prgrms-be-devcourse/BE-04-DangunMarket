package com.daangn.dangunmarket.domain.chat.repository.chatroom;

import com.daangn.dangunmarket.domain.chat.model.ChatRoom;

import java.util.Optional;

public interface ChatRoomRepository {

    ChatRoom save(ChatRoom chatRoom);

    Optional<ChatRoom> findById(Long chatRoomId);
}
