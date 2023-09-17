package com.daangn.dangunmarket.domain.chat.repository.chatroominfo;

import com.daangn.dangunmarket.domain.chat.model.ChatRoomInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ChatRoomInfoRepository {

    Slice<ChatRoomInfo> findMembersInSameChatRooms(Long memberId, Pageable pageable);

}
