package com.daangn.dangunmarket.domain.chat.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Entity
@Table(name = "chat_room_infos")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isWriter;

    @Column(nullable = false,name = "post_id")
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id", referencedColumnName = "id", nullable = false)
    private ChatRoom chatRoom;

    @Column(nullable = false, name = "member_id")
    private Long memberId;

    @Builder
    public ChatRoomInfo(boolean isWriter, Long postId, ChatRoom chatRoom, Long memberId) {
        Assert.notNull(postId, "postId는 null값이 들어올 수 없습니다.");
        Assert.notNull(chatRoom, "chatRoom는 null값이 들어올 수 없습니다.");
        Assert.notNull(memberId, "memberId는 null값이 들어올 수 없습니다.");

        this.isWriter = isWriter;
        this.postId = postId;
        this.chatRoom = chatRoom;
        this.memberId = memberId;
    }
}
