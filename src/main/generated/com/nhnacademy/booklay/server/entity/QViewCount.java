package com.nhnacademy.booklay.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QViewCount is a Querydsl query type for ViewCount
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QViewCount extends EntityPathBase<ViewCount> {

    private static final long serialVersionUID = -31043442L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QViewCount viewCount = new QViewCount("viewCount");

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QProduct product;

    public final DatePath<java.time.LocalDate> savedDate = createDate("savedDate", java.time.LocalDate.class);

    public QViewCount(String variable) {
        this(ViewCount.class, forVariable(variable), INITS);
    }

    public QViewCount(Path<? extends ViewCount> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QViewCount(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QViewCount(PathMetadata metadata, PathInits inits) {
        this(ViewCount.class, metadata, inits);
    }

    public QViewCount(Class<? extends ViewCount> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

