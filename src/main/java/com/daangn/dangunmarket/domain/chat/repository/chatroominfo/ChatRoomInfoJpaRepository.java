package com.daangn.dangunmarket.domain.chat.repository.chatroominfo;

import com.daangn.dangunmarket.domain.chat.model.ChatRoomInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomInfoJpaRepository extends JpaRepository<ChatRoomInfo, Long> {
}
