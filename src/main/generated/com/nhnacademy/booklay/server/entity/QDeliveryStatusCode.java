package com.nhnacademy.booklay.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDeliveryStatusCode is a Querydsl query type for DeliveryStatusCode
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDeliveryStatusCode extends EntityPathBase<DeliveryStatusCode> {

    private static final long serialVersionUID = -35701393L;

    public static final QDeliveryStatusCode deliveryStatusCode = new QDeliveryStatusCode("deliveryStatusCode");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public QDeliveryStatusCode(String variable) {
        super(DeliveryStatusCode.class, forVariable(variable));
    }

    public QDeliveryStatusCode(Path<? extends DeliveryStatusCode> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDeliveryStatusCode(PathMetadata metadata) {
        super(DeliveryStatusCode.class, metadata);
    }

}

