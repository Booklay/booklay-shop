package com.nhnacademy.booklay.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QWishlist_Pk is a Querydsl query type for Pk
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QWishlist_Pk extends BeanPath<Wishlist.Pk> {

    private static final long serialVersionUID = 382566664L;

    public static final QWishlist_Pk pk = new QWishlist_Pk("pk");

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    public QWishlist_Pk(String variable) {
        super(Wishlist.Pk.class, forVariable(variable));
    }

    public QWishlist_Pk(Path<? extends Wishlist.Pk> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWishlist_Pk(PathMetadata metadata) {
        super(Wishlist.Pk.class, metadata);
    }

}

