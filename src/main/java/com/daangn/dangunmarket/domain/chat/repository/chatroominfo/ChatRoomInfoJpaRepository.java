package com.daangn.dangunmarket.domain.chat.repository.chatroominfo;

import com.daangn.dangunmarket.domain.chat.model.ChatRoomInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomInfoJpaRepository extends JpaRepository<ChatRoomInfo, Long> {

    @Query("SELECT c FROM ChatRoomInfo c WHERE c.memberId=:memberId AND c.postId=:postId ")
    Optional<ChatRoomInfo> findChatRoomInfoByPostIdAndMemberId(@Param("postId") Long postId, @Param("memberId") Long memberId);
}
