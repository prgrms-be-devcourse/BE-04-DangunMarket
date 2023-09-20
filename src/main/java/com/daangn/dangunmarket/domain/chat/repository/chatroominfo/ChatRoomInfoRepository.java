package com.daangn.dangunmarket.domain.chat.repository.chatroominfo;

import com.daangn.dangunmarket.domain.chat.model.ChatRoomInfo;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.dto.JoinedMemberResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

public interface ChatRoomInfoRepository {

    Slice<JoinedMemberResponse> findMembersInSameChatRooms(Long memberId, Pageable pageable);

    Optional<ChatRoomInfo> findChatRoomInfoByPostIdAndMemberId(Long postId, Long memberId);

    ChatRoomInfo save(ChatRoomInfo chatRoomInfo);

}