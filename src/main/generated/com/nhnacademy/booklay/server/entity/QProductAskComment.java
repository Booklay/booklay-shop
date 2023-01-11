package com.nhnacademy.booklay.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductAskComment is a Querydsl query type for ProductAskComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductAskComment extends EntityPathBase<ProductAskComment> {

    private static final long serialVersionUID = 138113817L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductAskComment productAskComment = new QProductAskComment("productAskComment");

    public final NumberPath<Long> commentId = createNumber("commentId", Long.class);

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> depth = createNumber("depth", Long.class);

    public final QProductAskComment groupNo;

    public final NumberPath<Long> groupOrder = createNumber("groupOrder", Long.class);

    public final QMember memberId;

    public final QPost postId;

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QProductAskComment(String variable) {
        this(ProductAskComment.class, forVariable(variable), INITS);
    }

    public QProductAskComment(Path<? extends ProductAskComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductAskComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductAskComment(PathMetadata metadata, PathInits inits) {
        this(ProductAskComment.class, metadata, inits);
    }

    public QProductAskComment(Class<? extends ProductAskComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.groupNo = inits.isInitialized("groupNo") ? new QProductAskComment(forProperty("groupNo"), inits.get("groupNo")) : null;
        this.memberId = inits.isInitialized("memberId") ? new QMember(forProperty("memberId"), inits.get("memberId")) : null;
        this.postId = inits.isInitialized("postId") ? new QPost(forProperty("postId"), inits.get("postId")) : null;
    }

}

