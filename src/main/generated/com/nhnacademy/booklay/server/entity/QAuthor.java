package com.nhnacademy.booklay.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAuthor is a Querydsl query type for Author
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAuthor extends EntityPathBase<Author> {

    private static final long serialVersionUID = 1916553447L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAuthor author = new QAuthor("author");

    public final NumberPath<Long> authorNo = createNumber("authorNo", Long.class);

    public final QMember member;

    public final StringPath name = createString("name");

    public QAuthor(String variable) {
        this(Author.class, forVariable(variable), INITS);
    }

    public QAuthor(Path<? extends Author> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAuthor(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAuthor(PathMetadata metadata, PathInits inits) {
        this(Author.class, metadata, inits);
    }

    public QAuthor(Class<? extends Author> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

