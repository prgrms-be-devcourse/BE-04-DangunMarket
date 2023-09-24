package com.daangn.dangunmarket.domain.chat.repository.chatroominfo;

import com.daangn.dangunmarket.domain.chat.model.ChatRoomInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomInfoJpaRepository extends JpaRepository<ChatRoomInfo, Long> {

    @Query("SELECT c.memberId FROM ChatRoomInfo c WHERE c.chatRoom.id = :chatRoomId AND c.memberId != :myId")
    Long findSenderIdByChatRoomInfoAndMyId(@Param("chatRoomId") Long chatRoomId, @Param("myId") Long myId);

    @Query("SELECT DISTINCT c.postId FROM ChatRoomInfo c WHERE c.chatRoom.id = :chatRoomId")
    Long findPostIdByChatRoomId(@Param("chatRoomId") Long chatRoomId);

    @Query("SELECT c FROM ChatRoomInfo c WHERE c.memberId=:memberId AND c.postId=:postId AND c.isDeleted = false ")
    Optional<ChatRoomInfo> findChatRoomInfoByBuyer(@Param("postId") Long postId, @Param("memberId") Long memberId);

    @Query("SELECT c FROM ChatRoomInfo c " +
            "JOIN FETCH c.chatRoom " +
            "WHERE c.chatRoom.id = :chatRoomId AND c.isDeleted = false ")
    List<ChatRoomInfo> findChatRoomInfoByChatRoomId(@Param("chatRoomId") Long chatRoomId);

}
