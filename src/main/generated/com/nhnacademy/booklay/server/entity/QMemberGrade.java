package com.nhnacademy.booklay.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberGrade is a Querydsl query type for MemberGrade
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberGrade extends EntityPathBase<MemberGrade> {

    private static final long serialVersionUID = 645588865L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberGrade memberGrade = new QMemberGrade("memberGrade");

    public final DatePath<java.time.LocalDate> date = createDate("date", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public final StringPath name = createString("name");

    public QMemberGrade(String variable) {
        this(MemberGrade.class, forVariable(variable), INITS);
    }

    public QMemberGrade(Path<? extends MemberGrade> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberGrade(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberGrade(PathMetadata metadata, PathInits inits) {
        this(MemberGrade.class, metadata, inits);
    }

    public QMemberGrade(Class<? extends MemberGrade> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

