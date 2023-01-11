package com.nhnacademy.booklay.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRestockingNotification is a Querydsl query type for RestockingNotification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRestockingNotification extends EntityPathBase<RestockingNotification> {

    private static final long serialVersionUID = -2048077274L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRestockingNotification restockingNotification = new QRestockingNotification("restockingNotification");

    public final QMember member;

    public final QRestockingNotification_Pk pk;

    public final QProduct product;

    public QRestockingNotification(String variable) {
        this(RestockingNotification.class, forVariable(variable), INITS);
    }

    public QRestockingNotification(Path<? extends RestockingNotification> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRestockingNotification(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRestockingNotification(PathMetadata metadata, PathInits inits) {
        this(RestockingNotification.class, metadata, inits);
    }

    public QRestockingNotification(Class<? extends RestockingNotification> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
        this.pk = inits.isInitialized("pk") ? new QRestockingNotification_Pk(forProperty("pk")) : null;
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

