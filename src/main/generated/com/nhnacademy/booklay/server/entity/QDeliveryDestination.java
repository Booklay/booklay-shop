package com.nhnacademy.booklay.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDeliveryDestination is a Querydsl query type for DeliveryDestination
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDeliveryDestination extends EntityPathBase<DeliveryDestination> {

    private static final long serialVersionUID = -1619378786L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDeliveryDestination deliveryDestination = new QDeliveryDestination("deliveryDestination");

    public final StringPath address = createString("address");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDefaultDestination = createBoolean("isDefaultDestination");

    public final QMember member;

    public final StringPath name = createString("name");

    public final StringPath zipCode = createString("zipCode");

    public QDeliveryDestination(String variable) {
        this(DeliveryDestination.class, forVariable(variable), INITS);
    }

    public QDeliveryDestination(Path<? extends DeliveryDestination> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDeliveryDestination(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDeliveryDestination(PathMetadata metadata, PathInits inits) {
        this(DeliveryDestination.class, metadata, inits);
    }

    public QDeliveryDestination(Class<? extends DeliveryDestination> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

