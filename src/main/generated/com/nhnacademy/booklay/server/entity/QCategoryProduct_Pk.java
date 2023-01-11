package com.nhnacademy.booklay.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCategoryProduct_Pk is a Querydsl query type for Pk
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QCategoryProduct_Pk extends BeanPath<CategoryProduct.Pk> {

    private static final long serialVersionUID = 178389108L;

    public static final QCategoryProduct_Pk pk = new QCategoryProduct_Pk("pk");

    public final NumberPath<Long> categoryId = createNumber("categoryId", Long.class);

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    public QCategoryProduct_Pk(String variable) {
        super(CategoryProduct.Pk.class, forVariable(variable));
    }

    public QCategoryProduct_Pk(Path<? extends CategoryProduct.Pk> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCategoryProduct_Pk(PathMetadata metadata) {
        super(CategoryProduct.Pk.class, metadata);
    }

}

