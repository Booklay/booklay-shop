package com.nhnacademy.booklay.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProductTag_Pk is a Querydsl query type for Pk
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QProductTag_Pk extends BeanPath<ProductTag.Pk> {

    private static final long serialVersionUID = -56181790L;

    public static final QProductTag_Pk pk = new QProductTag_Pk("pk");

    public final NumberPath<Integer> productId = createNumber("productId", Integer.class);

    public final NumberPath<Integer> tagId = createNumber("tagId", Integer.class);

    public QProductTag_Pk(String variable) {
        super(ProductTag.Pk.class, forVariable(variable));
    }

    public QProductTag_Pk(Path<? extends ProductTag.Pk> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductTag_Pk(PathMetadata metadata) {
        super(ProductTag.Pk.class, metadata);
    }

}

