package com.daangn.dangunmarket.domain.chat.model;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "chat_messages")
@Getter
@NoArgsConstructor
public class ChatMessage {

    @Id
    private String id;

    @Field("chat_room_id")
    private Long chatRoomId;

    @Field("member_id")
    private Long memberId;

    @Field("message")
    private String message;

    @Field("image_urls")
    private List<String> imageUrls = new ArrayList<>();

    @Field("read_or_not")
    private Integer readOrNot;

    @Field("created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Field("type")
    private MessageType type;

    @Builder
    public ChatMessage(Long chatRoomId, Long memberId, String message, List<String> imageUrls, Integer readOrNot, MessageType type) {
        this.chatRoomId = chatRoomId;
        this.memberId = memberId;
        this.message = message;
        this.imageUrls = imageUrls;
        this.readOrNot = readOrNot;
        this.type = type;
    }

}
