package com.nhnacademy.booklay.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBlockedMemberDetail is a Querydsl query type for BlockedMemberDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBlockedMemberDetail extends EntityPathBase<BlockedMemberDetail> {

    private static final long serialVersionUID = 1279464891L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBlockedMemberDetail blockedMemberDetail = new QBlockedMemberDetail("blockedMemberDetail");

    public final DateTimePath<java.time.LocalDateTime> blockedAt = createDateTime("blockedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public final StringPath reason = createString("reason");

    public final DateTimePath<java.time.LocalDateTime> releasedAt = createDateTime("releasedAt", java.time.LocalDateTime.class);

    public QBlockedMemberDetail(String variable) {
        this(BlockedMemberDetail.class, forVariable(variable), INITS);
    }

    public QBlockedMemberDetail(Path<? extends BlockedMemberDetail> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBlockedMemberDetail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBlockedMemberDetail(PathMetadata metadata, PathInits inits) {
        this(BlockedMemberDetail.class, metadata, inits);
    }

    public QBlockedMemberDetail(Class<? extends BlockedMemberDetail> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

