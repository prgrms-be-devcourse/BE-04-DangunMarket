package com.daangn.dangunmarket.domain.chat.model;

import com.daangn.dangunmarket.domain.member.model.Member;
import com.daangn.dangunmarket.domain.post.model.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_room_infos")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isWriter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "products_id", referencedColumnName = "id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatrooms_id", referencedColumnName = "id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "members_id", referencedColumnName = "id", nullable = false)
    private Member member;

    public ChatRoomInfo(boolean isWriter, Post post, ChatRoom chatRoom, Member member) {
        this.isWriter = isWriter;
        this.post = post;
        this.chatRoom = chatRoom;
        this.member = member;
    }

}
