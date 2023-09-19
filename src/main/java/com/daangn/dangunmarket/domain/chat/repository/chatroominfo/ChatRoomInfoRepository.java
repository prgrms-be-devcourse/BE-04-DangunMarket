package com.daangn.dangunmarket.domain.chat.repository.chatroominfo;

import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.dto.JoinedMemberResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ChatRoomInfoRepository {

    Slice<JoinedMemberResponse> findMembersInSameChatRooms(Long memberId, Pageable pageable);

}
