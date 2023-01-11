package com.nhnacademy.booklay.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrder is a Querydsl query type for Order
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrder extends EntityPathBase<Order> {

    private static final long serialVersionUID = -2142108558L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrder order = new QOrder("order1");

    public final NumberPath<Long> deliveryPrice = createNumber("deliveryPrice", Long.class);

    public final NumberPath<Long> discountPrice = createNumber("discountPrice", Long.class);

    public final NumberPath<Long> giftWrappingPrice = createNumber("giftWrappingPrice", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isBlinded = createBoolean("isBlinded");

    public final QMember member;

    public final DateTimePath<java.time.LocalDateTime> orderedAt = createDateTime("orderedAt", java.time.LocalDateTime.class);

    public final QOrderStatusCode orderStatusCode;

    public final NumberPath<Long> paymentMethod = createNumber("paymentMethod", Long.class);

    public final NumberPath<Long> paymentPrice = createNumber("paymentPrice", Long.class);

    public final NumberPath<Long> pointUsePrice = createNumber("pointUsePrice", Long.class);

    public final NumberPath<Long> productPriceSum = createNumber("productPriceSum", Long.class);

    public QOrder(String variable) {
        this(Order.class, forVariable(variable), INITS);
    }

    public QOrder(Path<? extends Order> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrder(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrder(PathMetadata metadata, PathInits inits) {
        this(Order.class, metadata, inits);
    }

    public QOrder(Class<? extends Order> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
        this.orderStatusCode = inits.isInitialized("orderStatusCode") ? new QOrderStatusCode(forProperty("orderStatusCode")) : null;
    }

}

