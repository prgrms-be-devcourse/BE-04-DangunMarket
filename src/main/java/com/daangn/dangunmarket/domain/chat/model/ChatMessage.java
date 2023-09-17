package com.daangn.dangunmarket.domain.chat.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;

@Document(collection = "chat_messages")
@Getter
@NoArgsConstructor
public class ChatMessage {

    @Id
    @Field(value = "_id", targetType = FieldType.OBJECT_ID)
    private String id;

    @Field("chat_room_info_id")
    private Long chatRoomInfoId;

    @Field("sender_name")
    private String senderName;

    @Field("member_id")
    private Long memberId;

    @Field("message")
    private String message;

    @Field("img_url")
    private String imgUrl;

    @Field("read_or_not")
    private boolean readOrNot;

    @Field("created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    public ChatMessage(Long chatRoomInfoId, String senderName, Long memberId, String message, String imgUrl, boolean readOrNot) {
        this.chatRoomInfoId = chatRoomInfoId;
        this.senderName = senderName;
        this.memberId = memberId;
        this.message = message;
        this.imgUrl = imgUrl;
        this.readOrNot = readOrNot;
    }

}
