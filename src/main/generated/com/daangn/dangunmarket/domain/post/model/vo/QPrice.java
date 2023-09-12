package com.daangn.dangunmarket.domain.post.model.vo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPrice is a Querydsl query type for Price
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QPrice extends BeanPath<Price> {

    private static final long serialVersionUID = 1910605680L;

    public static final QPrice price = new QPrice("price");

    public final NumberPath<Long> value = createNumber("value", Long.class);

    public QPrice(String variable) {
        super(Price.class, forVariable(variable));
    }

    public QPrice(Path<? extends Price> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPrice(PathMetadata metadata) {
        super(Price.class, metadata);
    }

}

