package com.nhnacademy.booklay.server.entity;

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

    private static final long serialVersionUID = -346167556L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> depth = createNumber("depth", Long.class);

    public final QPost groupNo;

    public final NumberPath<Long> groupOrder = createNumber("groupOrder", Long.class);

    public final BooleanPath isAnswered = createBoolean("isAnswered");

    public final BooleanPath isViewPublic = createBoolean("isViewPublic");

    public final QMember memberId;

    public final NumberPath<Long> postId = createNumber("postId", Long.class);

    public final QPostType postTypeId;

    public final QProduct productId;

    public final StringPath title = createString("title");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

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
        this.groupNo = inits.isInitialized("groupNo") ? new QPost(forProperty("groupNo"), inits.get("groupNo")) : null;
        this.memberId = inits.isInitialized("memberId") ? new QMember(forProperty("memberId"), inits.get("memberId")) : null;
        this.postTypeId = inits.isInitialized("postTypeId") ? new QPostType(forProperty("postTypeId")) : null;
        this.productId = inits.isInitialized("productId") ? new QProduct(forProperty("productId"), inits.get("productId")) : null;
    }

}

