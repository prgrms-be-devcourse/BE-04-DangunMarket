package com.daangn.dangunmarket.domain.member.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QActivityArea is a Querydsl query type for ActivityArea
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QActivityArea extends EntityPathBase<ActivityArea> {

    private static final long serialVersionUID = -1948560994L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QActivityArea activityArea = new QActivityArea("activityArea");

    public final NumberPath<Long> emdAreaId = createNumber("emdAreaId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isVerified = createBoolean("isVerified");

    public final QMember member;

    public QActivityArea(String variable) {
        this(ActivityArea.class, forVariable(variable), INITS);
    }

    public QActivityArea(Path<? extends ActivityArea> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QActivityArea(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QActivityArea(PathMetadata metadata, PathInits inits) {
        this(ActivityArea.class, metadata, inits);
    }

    public QActivityArea(Class<? extends ActivityArea> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

