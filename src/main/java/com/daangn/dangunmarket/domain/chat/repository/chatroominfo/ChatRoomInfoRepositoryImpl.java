package com.daangn.dangunmarket.domain.chat.repository.chatroominfo;

import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.dto.JoinedMemberResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public class ChatRoomInfoRepositoryImpl implements ChatRoomInfoRepository {

    private final ChatRoomInfoJpaRepository chatRoomInfoJpaRepository;
    private final ChatRoomInfoQueryRepository chatRoomInfoQueryRepository;

    public ChatRoomInfoRepositoryImpl(ChatRoomInfoJpaRepository chatRoomInfoJpaRepository, ChatRoomInfoQueryRepository chatRoomInfoQueryRepository) {
        this.chatRoomInfoJpaRepository = chatRoomInfoJpaRepository;
        this.chatRoomInfoQueryRepository = chatRoomInfoQueryRepository;
    }

    @Override
    public Slice<JoinedMemberResponse> findMembersInSameChatRooms(Long memberId, Pageable pageable) {
        return chatRoomInfoQueryRepository.findMembersInSameChatRooms(memberId, pageable);
    }
}
