package com.daangn.dangunmarket.domain.chat.repository.chatroominfo;

import com.daangn.dangunmarket.domain.chat.model.QChatRoomInfo;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.dto.JoinedMemberResponse;

import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.dto.QJoinedMemberResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.daangn.dangunmarket.domain.member.model.QMember.member;

@Repository
public class ChatRoomInfoQueryRepository {

    private final JPAQueryFactory queryFactory;

    public ChatRoomInfoQueryRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public Slice<JoinedMemberResponse> findMembersInSameChatRooms(Long memberId, Pageable pageable) {
        QChatRoomInfo chatRoomInfo = QChatRoomInfo.chatRoomInfo;
        QChatRoomInfo otherChatRoomInfo = new QChatRoomInfo("other");
        int pageSize = pageable.getPageSize();

        List<JoinedMemberResponse> contents = queryFactory
                .select(new QJoinedMemberResponse(otherChatRoomInfo, member))
                .from(chatRoomInfo)
                .join(chatRoomInfo).on(chatRoomInfo.chatRoom.id.eq(otherChatRoomInfo.chatRoom.id))
                .join(member).on(otherChatRoomInfo.memberId.eq(member.id))
                .where(chatRoomInfo.memberId.eq(memberId), otherChatRoomInfo.memberId.ne(memberId))
                .offset(pageable.getOffset())
                .limit(pageSize + 1)
                .fetch();

        boolean hasNext = false;
        if (contents.size() > pageSize){
            contents.remove(pageSize);
            hasNext = true;
        }

        return new SliceImpl<>(contents, pageable, hasNext);
    }
}
