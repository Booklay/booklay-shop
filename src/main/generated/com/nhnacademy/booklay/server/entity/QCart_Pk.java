package com.nhnacademy.booklay.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCart_Pk is a Querydsl query type for Pk
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QCart_Pk extends BeanPath<Cart.Pk> {

    private static final long serialVersionUID = 484486093L;

    public static final QCart_Pk pk = new QCart_Pk("pk");

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    public QCart_Pk(String variable) {
        super(Cart.Pk.class, forVariable(variable));
    }

    public QCart_Pk(Path<? extends Cart.Pk> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCart_Pk(PathMetadata metadata) {
        super(Cart.Pk.class, metadata);
    }

}

