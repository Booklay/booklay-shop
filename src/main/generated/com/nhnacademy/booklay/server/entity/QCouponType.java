package com.nhnacademy.booklay.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCouponType is a Querydsl query type for CouponType
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCouponType extends EntityPathBase<CouponType> {

    private static final long serialVersionUID = 1972007388L;

    public static final QCouponType couponType = new QCouponType("couponType");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QCouponType(String variable) {
        super(CouponType.class, forVariable(variable));
    }

    public QCouponType(Path<? extends CouponType> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCouponType(PathMetadata metadata) {
        super(CouponType.class, metadata);
    }

}

