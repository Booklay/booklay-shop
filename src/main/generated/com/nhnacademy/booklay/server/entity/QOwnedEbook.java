package com.nhnacademy.booklay.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOwnedEbook is a Querydsl query type for OwnedEbook
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOwnedEbook extends EntityPathBase<OwnedEbook> {

    private static final long serialVersionUID = 1494324869L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOwnedEbook ownedEbook = new QOwnedEbook("ownedEbook");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public final QProduct product;

    public QOwnedEbook(String variable) {
        this(OwnedEbook.class, forVariable(variable), INITS);
    }

    public QOwnedEbook(Path<? extends OwnedEbook> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOwnedEbook(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOwnedEbook(PathMetadata metadata, PathInits inits) {
        this(OwnedEbook.class, metadata, inits);
    }

    public QOwnedEbook(Class<? extends OwnedEbook> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

