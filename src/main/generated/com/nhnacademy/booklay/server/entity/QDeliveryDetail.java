package com.nhnacademy.booklay.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDeliveryDetail is a Querydsl query type for DeliveryDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDeliveryDetail extends EntityPathBase<DeliveryDetail> {

    private static final long serialVersionUID = 2003068097L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDeliveryDetail deliveryDetail = new QDeliveryDetail("deliveryDetail");

    public final StringPath address = createString("address");

    public final DateTimePath<java.time.LocalDateTime> completedAt = createDateTime("completedAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> deliveryStartAt = createDateTime("deliveryStartAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath invoiceNo = createString("invoiceNo");

    public final StringPath memo = createString("memo");

    public final QOrder order;

    public final StringPath receiver = createString("receiver");

    public final StringPath receiverPhoneNumber = createString("receiverPhoneNumber");

    public final StringPath sender = createString("sender");

    public final StringPath senderPhoneNumber = createString("senderPhoneNumber");

    public final QDeliveryStatusCode statusCode;

    public final StringPath zipCode = createString("zipCode");

    public QDeliveryDetail(String variable) {
        this(DeliveryDetail.class, forVariable(variable), INITS);
    }

    public QDeliveryDetail(Path<? extends DeliveryDetail> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDeliveryDetail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDeliveryDetail(PathMetadata metadata, PathInits inits) {
        this(DeliveryDetail.class, metadata, inits);
    }

    public QDeliveryDetail(Class<? extends DeliveryDetail> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.order = inits.isInitialized("order") ? new QOrder(forProperty("order"), inits.get("order")) : null;
        this.statusCode = inits.isInitialized("statusCode") ? new QDeliveryStatusCode(forProperty("statusCode")) : null;
    }

}

