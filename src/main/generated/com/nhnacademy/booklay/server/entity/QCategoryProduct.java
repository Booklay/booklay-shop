package com.nhnacademy.booklay.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCategoryProduct is a Querydsl query type for CategoryProduct
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCategoryProduct extends EntityPathBase<CategoryProduct> {

    private static final long serialVersionUID = 1263799861L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCategoryProduct categoryProduct = new QCategoryProduct("categoryProduct");

    public final QCategory category;

    public final QCategoryProduct_Pk pk;

    public final QProduct product;

    public QCategoryProduct(String variable) {
        this(CategoryProduct.class, forVariable(variable), INITS);
    }

    public QCategoryProduct(Path<? extends CategoryProduct> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCategoryProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCategoryProduct(PathMetadata metadata, PathInits inits) {
        this(CategoryProduct.class, metadata, inits);
    }

    public QCategoryProduct(Class<? extends CategoryProduct> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new QCategory(forProperty("category"), inits.get("category")) : null;
        this.pk = inits.isInitialized("pk") ? new QCategoryProduct_Pk(forProperty("pk")) : null;
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

