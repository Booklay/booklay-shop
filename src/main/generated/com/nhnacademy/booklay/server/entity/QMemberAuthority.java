package com.nhnacademy.booklay.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberAuthority is a Querydsl query type for MemberAuthority
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberAuthority extends EntityPathBase<MemberAuthority> {

    private static final long serialVersionUID = -897693203L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberAuthority memberAuthority = new QMemberAuthority("memberAuthority");

    public final QAuthority authority;

    public final QMember member;

    public final QMemberAuthority_Pk pk;

    public QMemberAuthority(String variable) {
        this(MemberAuthority.class, forVariable(variable), INITS);
    }

    public QMemberAuthority(Path<? extends MemberAuthority> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberAuthority(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberAuthority(PathMetadata metadata, PathInits inits) {
        this(MemberAuthority.class, metadata, inits);
    }

    public QMemberAuthority(Class<? extends MemberAuthority> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.authority = inits.isInitialized("authority") ? new QAuthority(forProperty("authority")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
        this.pk = inits.isInitialized("pk") ? new QMemberAuthority_Pk(forProperty("pk")) : null;
    }

}

