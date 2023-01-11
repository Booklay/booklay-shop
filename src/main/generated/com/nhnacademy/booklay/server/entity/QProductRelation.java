package com.nhnacademy.booklay.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductRelation is a Querydsl query type for ProductRelation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductRelation extends EntityPathBase<ProductRelation> {

    private static final long serialVersionUID = 2099588143L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductRelation productRelation = new QProductRelation("productRelation");

    public final QProduct baseProduct;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final QProduct relatedProduct;

    public QProductRelation(String variable) {
        this(ProductRelation.class, forVariable(variable), INITS);
    }

    public QProductRelation(Path<? extends ProductRelation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductRelation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductRelation(PathMetadata metadata, PathInits inits) {
        this(ProductRelation.class, metadata, inits);
    }

    public QProductRelation(Class<? extends ProductRelation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.baseProduct = inits.isInitialized("baseProduct") ? new QProduct(forProperty("baseProduct"), inits.get("baseProduct")) : null;
        this.relatedProduct = inits.isInitialized("relatedProduct") ? new QProduct(forProperty("relatedProduct"), inits.get("relatedProduct")) : null;
    }

}

