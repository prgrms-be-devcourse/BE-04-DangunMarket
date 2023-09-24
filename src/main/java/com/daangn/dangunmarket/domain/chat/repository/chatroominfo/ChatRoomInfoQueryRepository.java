package com.daangn.dangunmarket.domain.chat.repository.chatroominfo;

import com.daangn.dangunmarket.domain.chat.model.QChatRoomInfo;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.dto.JoinedMemberResponse;

import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.dto.JoinedPostWithMemberResponse;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.dto.QJoinedMemberResponse;
import com.daangn.dangunmarket.domain.chat.repository.chatroominfo.dto.QJoinedPostWithMemberResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.daangn.dangunmarket.domain.member.model.QMember.member;
import static com.daangn.dangunmarket.domain.post.model.QPost.post;

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
                .from(chatRoomInfo, otherChatRoomInfo)
                .join(member).on(otherChatRoomInfo.memberId.eq(member.id))
                .where(
                        chatRoomInfo.chatRoom.eq(otherChatRoomInfo.chatRoom),
                        chatRoomInfo.isDeleted.eq(false),
                        chatRoomInfo.memberId.eq(memberId),
                        otherChatRoomInfo.memberId.ne(memberId))
                .offset(pageable.getOffset())
                .limit(pageSize + 1)
                .fetch();

        boolean hasNext = false;
        if (contents.size() > pageSize) {
            contents.remove(pageSize);
            hasNext = true;
        }

        return new SliceImpl<>(contents, pageable, hasNext);
    }

    public List<JoinedPostWithMemberResponse> findPostWithMember(Long chatRoomId) {
        QChatRoomInfo chatRoomInfo = QChatRoomInfo.chatRoomInfo;

        return queryFactory
                .select(new QJoinedPostWithMemberResponse(member, post))
                .from(chatRoomInfo)
                .join(member).on(chatRoomInfo.memberId.eq(member.id))
                .join(post).on(chatRoomInfo.postId.eq(post.id))
                .where(
                        chatRoomInfo.chatRoom.id.eq(chatRoomId),
                        chatRoomInfo.isWriter.eq(true)
                )
                .fetch();
    }

}
