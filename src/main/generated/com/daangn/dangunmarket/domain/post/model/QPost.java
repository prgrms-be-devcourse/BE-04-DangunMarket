package com.daangn.dangunmarket.domain.post.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = 1487389820L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final com.daangn.dangunmarket.global.entity.QBaseEntity _super = new com.daangn.dangunmarket.global.entity.QBaseEntity(this);

    public final NumberPath<Long> areaId = createNumber("areaId", Long.class);

    public final QCategory category;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final BooleanPath isDeleted = _super.isDeleted;

    public final BooleanPath isOfferAllowed = createBoolean("isOfferAllowed");

    public final NumberPath<Integer> likeCount = createNumber("likeCount", Integer.class);

    public final QLocationPreference localPreference;

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final ListPath<PostImage, QPostImage> postImageList = this.<PostImage, QPostImage>createList("postImageList", PostImage.class, QPostImage.class, PathInits.DIRECT2);

    public final com.daangn.dangunmarket.domain.post.model.vo.QPrice price;

    public final DateTimePath<java.time.LocalDateTime> refreshedAt = createDateTime("refreshedAt", java.time.LocalDateTime.class);

    public final com.daangn.dangunmarket.domain.post.model.vo.QTitle title;

    public final EnumPath<TradeStatus> tradeStatus = createEnum("tradeStatus", TradeStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new QCategory(forProperty("category"), inits.get("category")) : null;
        this.localPreference = inits.isInitialized("localPreference") ? new QLocationPreference(forProperty("localPreference")) : null;
        this.price = inits.isInitialized("price") ? new com.daangn.dangunmarket.domain.post.model.vo.QPrice(forProperty("price")) : null;
        this.title = inits.isInitialized("title") ? new com.daangn.dangunmarket.domain.post.model.vo.QTitle(forProperty("title")) : null;
    }

}

