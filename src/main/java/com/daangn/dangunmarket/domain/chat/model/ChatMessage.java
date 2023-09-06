package com.daangn.dangunmarket.domain.chat.model;

import com.daangn.dangunmarket.domain.chat.model.ChatRoomInfo;
import com.daangn.dangunmarket.domain.member.model.Member;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_messages")
@NoArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean readOrNot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_infos_id", referencedColumnName = "id", nullable = false)
    private ChatRoomInfo chatRoomInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "members_id", referencedColumnName = "id", nullable = false)
    private Member member;

    public ChatMessage(String content, boolean readOrNot, ChatRoomInfo chatRoomInfo, Member member) {
        this.content = content;
        this.readOrNot = readOrNot;
        this.chatRoomInfo = chatRoomInfo;
        this.member = member;
    }

}
