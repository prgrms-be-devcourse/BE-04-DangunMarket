package com.daangn.dangunmarket.domain.post.model.vo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTitle is a Querydsl query type for Title
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QTitle extends BeanPath<Title> {

    private static final long serialVersionUID = 1914042495L;

    public static final QTitle title1 = new QTitle("title1");

    public final StringPath title = createString("title");

    public QTitle(String variable) {
        super(Title.class, forVariable(variable));
    }

    public QTitle(Path<? extends Title> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTitle(PathMetadata metadata) {
        super(Title.class, metadata);
    }

}

