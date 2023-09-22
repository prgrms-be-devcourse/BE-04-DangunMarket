package com.daangn.dangunmarket.domain.chat.repository.chatroominfo;

import com.daangn.dangunmarket.domain.chat.model.ChatRoomInfo;

import java.util.Optional;

public interface ChatRoomInfoRepository {

    Optional<ChatRoomInfo> findChatRoomInfoByPostIdAndMemberId(Long postId, Long memberId);

    ChatRoomInfo save(ChatRoomInfo chatRoomInfo);

    Long findSenderIdByChatRoomInfoAndMyId(Long chatRoomId, Long myId);

    Long findPostIdByChatRoomId(Long chatRoomId);

}
