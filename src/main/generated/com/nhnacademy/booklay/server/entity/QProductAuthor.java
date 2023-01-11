package com.nhnacademy.booklay.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductAuthor is a Querydsl query type for ProductAuthor
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductAuthor extends EntityPathBase<ProductAuthor> {

    private static final long serialVersionUID = -581221250L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductAuthor productAuthor = new QProductAuthor("productAuthor");

    public final QAuthor author;

    public final QProductAuthor_Pk pk;

    public final QProductDetail productDetail;

    public QProductAuthor(String variable) {
        this(ProductAuthor.class, forVariable(variable), INITS);
    }

    public QProductAuthor(Path<? extends ProductAuthor> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductAuthor(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductAuthor(PathMetadata metadata, PathInits inits) {
        this(ProductAuthor.class, metadata, inits);
    }

    public QProductAuthor(Class<? extends ProductAuthor> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new QAuthor(forProperty("author"), inits.get("author")) : null;
        this.pk = inits.isInitialized("pk") ? new QProductAuthor_Pk(forProperty("pk")) : null;
        this.productDetail = inits.isInitialized("productDetail") ? new QProductDetail(forProperty("productDetail"), inits.get("productDetail")) : null;
    }

}

