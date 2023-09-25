package com.daangn.dangunmarket.domain.chat.repository.chatroominfo.dto;

import com.daangn.dangunmarket.domain.chat.model.ChatRoomInfo;
import com.daangn.dangunmarket.domain.member.model.Member;
import com.querydsl.core.annotations.QueryProjection;

public record JoinedMemberResponse(
        ChatRoomInfo chatRoomInfo,
        Member member) {

    @QueryProjection
    public JoinedMemberResponse(ChatRoomInfo chatRoomInfo, Member member) {
        this.chatRoomInfo = chatRoomInfo;
        this.member = member;
    }
}
