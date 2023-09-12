package com.daangn.dangunmarket.domain.chat.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChatRoomInfo is a Querydsl query type for ChatRoomInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatRoomInfo extends EntityPathBase<ChatRoomInfo> {

    private static final long serialVersionUID = -118751803L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChatRoomInfo chatRoomInfo = new QChatRoomInfo("chatRoomInfo");

    public final QChatRoom chatroom;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isWriter = createBoolean("isWriter");

    public final com.daangn.dangunmarket.domain.member.model.QMember member;

    public final com.daangn.dangunmarket.domain.post.model.QPost post;

    public QChatRoomInfo(String variable) {
        this(ChatRoomInfo.class, forVariable(variable), INITS);
    }

    public QChatRoomInfo(Path<? extends ChatRoomInfo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChatRoomInfo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChatRoomInfo(PathMetadata metadata, PathInits inits) {
        this(ChatRoomInfo.class, metadata, inits);
    }

    public QChatRoomInfo(Class<? extends ChatRoomInfo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.chatroom = inits.isInitialized("chatroom") ? new QChatRoom(forProperty("chatroom")) : null;
        this.member = inits.isInitialized("member") ? new com.daangn.dangunmarket.domain.member.model.QMember(forProperty("member"), inits.get("member")) : null;
        this.post = inits.isInitialized("post") ? new com.daangn.dangunmarket.domain.post.model.QPost(forProperty("post"), inits.get("post")) : null;
    }

}

