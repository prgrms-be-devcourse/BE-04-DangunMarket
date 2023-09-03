package com.daangn.dangunmarket.domain.chat.chatRoomInfo.domain;

import com.daangn.dangunmarket.domain.chat.chatRoom.domain.ChatRoom;
import com.daangn.dangunmarket.domain.member.member.domain.Member;
import com.daangn.dangunmarket.domain.product.product.domain.Product;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_room_infos")
@NoArgsConstructor
public class ChatRoomInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isWriter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "products_id", referencedColumnName = "id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatrooms_id", referencedColumnName = "id", nullable = false)
    private ChatRoom chatroom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "members_id", referencedColumnName = "id", nullable = false)
    private Member member;

    public ChatRoomInfo(boolean isWriter, Product product, ChatRoom chatroom, Member member) {
        this.isWriter = isWriter;
        this.product = product;
        this.chatroom = chatroom;
        this.member = member;
    }

}
