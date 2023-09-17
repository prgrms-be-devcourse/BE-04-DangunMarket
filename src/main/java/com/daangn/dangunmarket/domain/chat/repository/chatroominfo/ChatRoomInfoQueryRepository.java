package com.daangn.dangunmarket.domain.chat.repository.chatroominfo;

import com.daangn.dangunmarket.domain.chat.model.ChatRoomInfo;
import com.daangn.dangunmarket.domain.chat.model.QChatRoomInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChatRoomInfoQueryRepository {

    private final JPAQueryFactory queryFactory;

    public ChatRoomInfoQueryRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public Slice<ChatRoomInfo> findMembersInSameChatRooms(Long memberId, Pageable pageable) {
        QChatRoomInfo chatRoomInfo = QChatRoomInfo.chatRoomInfo;
        QChatRoomInfo otherChatRoomInfo = new QChatRoomInfo("other");
        int pageSize = pageable.getPageSize();

        List<ChatRoomInfo> contents = queryFactory
                .selectFrom(otherChatRoomInfo)
                .join(otherChatRoomInfo.member).fetchJoin()
                .join(chatRoomInfo).on(chatRoomInfo.chatroom.id.eq(otherChatRoomInfo.chatroom.id))
                .where(chatRoomInfo.member.id.eq(memberId), otherChatRoomInfo.member.id.ne(memberId))
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
