package com.daangn.dangunmarket.domain.chat.repository.chatroominfo.dto;

import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.post.model.Post;
import com.querydsl.core.annotations.QueryProjection;

public record JoinedPostWithMemberResponse(
        Member member,
        Post post
){
    @QueryProjection
    public JoinedPostWithMemberResponse(Member member, Post post) {
        this.member = member;
        this.post = post;
    }
}
