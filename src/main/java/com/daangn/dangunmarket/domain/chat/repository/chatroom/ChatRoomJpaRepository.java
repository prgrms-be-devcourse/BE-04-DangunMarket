package com.daangn.dangunmarket.domain.chat.repository.chatroom;

import com.daangn.dangunmarket.domain.chat.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoom, Long> {
}
