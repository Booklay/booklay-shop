package com.nhnacademy.booklay.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPostType is a Querydsl query type for PostType
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostType extends EntityPathBase<PostType> {

    private static final long serialVersionUID = -1409151914L;

    public static final QPostType postType = new QPostType("postType");

    public final NumberPath<Long> postTypeId = createNumber("postTypeId", Long.class);

    public final StringPath type = createString("type");

    public QPostType(String variable) {
        super(PostType.class, forVariable(variable));
    }

    public QPostType(Path<? extends PostType> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPostType(PathMetadata metadata) {
        super(PostType.class, metadata);
    }

}

