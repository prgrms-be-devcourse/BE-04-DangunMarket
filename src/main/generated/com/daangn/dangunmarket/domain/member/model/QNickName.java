package com.daangn.dangunmarket.domain.member.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QNickName is a Querydsl query type for NickName
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QNickName extends BeanPath<NickName> {

    private static final long serialVersionUID = 1933465328L;

    public static final QNickName nickName1 = new QNickName("nickName1");

    public final StringPath nickName = createString("nickName");

    public QNickName(String variable) {
        super(NickName.class, forVariable(variable));
    }

    public QNickName(Path<? extends NickName> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNickName(PathMetadata metadata) {
        super(NickName.class, metadata);
    }

}

