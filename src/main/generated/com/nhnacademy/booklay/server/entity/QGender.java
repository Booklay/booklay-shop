package com.nhnacademy.booklay.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGender is a Querydsl query type for Gender
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGender extends EntityPathBase<Gender> {

    private static final long serialVersionUID = 2073369117L;

    public static final QGender gender = new QGender("gender");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QGender(String variable) {
        super(Gender.class, forVariable(variable));
    }

    public QGender(Path<? extends Gender> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGender(PathMetadata metadata) {
        super(Gender.class, metadata);
    }

}

