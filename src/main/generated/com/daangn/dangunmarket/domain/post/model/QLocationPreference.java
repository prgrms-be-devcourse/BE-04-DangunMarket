package com.daangn.dangunmarket.domain.post.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLocationPreference is a Querydsl query type for LocationPreference
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLocationPreference extends EntityPathBase<LocationPreference> {

    private static final long serialVersionUID = -240052916L;

    public static final QLocationPreference locationPreference = new QLocationPreference("locationPreference");

    public final StringPath alias = createString("alias");

    public final ComparablePath<org.locationtech.jts.geom.Point> geography = createComparable("geography", org.locationtech.jts.geom.Point.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QLocationPreference(String variable) {
        super(LocationPreference.class, forVariable(variable));
    }

    public QLocationPreference(Path<? extends LocationPreference> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLocationPreference(PathMetadata metadata) {
        super(LocationPreference.class, metadata);
    }

}

