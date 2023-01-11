package com.nhnacademy.booklay.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRestockingNotification_Pk is a Querydsl query type for Pk
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QRestockingNotification_Pk extends BeanPath<RestockingNotification.Pk> {

    private static final long serialVersionUID = 35384035L;

    public static final QRestockingNotification_Pk pk = new QRestockingNotification_Pk("pk");

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    public QRestockingNotification_Pk(String variable) {
        super(RestockingNotification.Pk.class, forVariable(variable));
    }

    public QRestockingNotification_Pk(Path<? extends RestockingNotification.Pk> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRestockingNotification_Pk(PathMetadata metadata) {
        super(RestockingNotification.Pk.class, metadata);
    }

}

