package com.nhnacademy.booklay.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMemberAuthority_Pk is a Querydsl query type for Pk
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QMemberAuthority_Pk extends BeanPath<MemberAuthority.Pk> {

    private static final long serialVersionUID = 1583188412L;

    public static final QMemberAuthority_Pk pk = new QMemberAuthority_Pk("pk");

    public final NumberPath<Long> authorityId = createNumber("authorityId", Long.class);

    public final NumberPath<Long> memberNo = createNumber("memberNo", Long.class);

    public QMemberAuthority_Pk(String variable) {
        super(MemberAuthority.Pk.class, forVariable(variable));
    }

    public QMemberAuthority_Pk(Path<? extends MemberAuthority.Pk> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMemberAuthority_Pk(PathMetadata metadata) {
        super(MemberAuthority.Pk.class, metadata);
    }

}

