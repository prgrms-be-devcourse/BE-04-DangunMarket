package com.daangn.dangunmarket.domain.chat.repository.chatroom;

import com.daangn.dangunmarket.domain.chat.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoom, Long> {

    @Query("SELECT c FROM ChatRoom c WHERE c.id = :chatRoomId AND c.isDeleted = false ")
    Optional<ChatRoom> findById(@Param("chatRoomId") Long chatRoomId);

}
