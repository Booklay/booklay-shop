package com.nhnacademy.booklay.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QOrderStatusCode is a Querydsl query type for OrderStatusCode
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderStatusCode extends EntityPathBase<OrderStatusCode> {

    private static final long serialVersionUID = 671556369L;

    public static final QOrderStatusCode orderStatusCode = new QOrderStatusCode("orderStatusCode");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QOrderStatusCode(String variable) {
        super(OrderStatusCode.class, forVariable(variable));
    }

    public QOrderStatusCode(Path<? extends OrderStatusCode> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOrderStatusCode(PathMetadata metadata) {
        super(OrderStatusCode.class, metadata);
    }

}

