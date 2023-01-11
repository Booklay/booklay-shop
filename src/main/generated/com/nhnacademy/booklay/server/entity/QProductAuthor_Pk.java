package com.nhnacademy.booklay.server.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProductAuthor_Pk is a Querydsl query type for Pk
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QProductAuthor_Pk extends BeanPath<ProductAuthor.Pk> {

    private static final long serialVersionUID = 2145925515L;

    public static final QProductAuthor_Pk pk = new QProductAuthor_Pk("pk");

    public final NumberPath<Long> authorId = createNumber("authorId", Long.class);

    public final NumberPath<Long> bookId = createNumber("bookId", Long.class);

    public QProductAuthor_Pk(String variable) {
        super(ProductAuthor.Pk.class, forVariable(variable));
    }

    public QProductAuthor_Pk(Path<? extends ProductAuthor.Pk> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductAuthor_Pk(PathMetadata metadata) {
        super(ProductAuthor.Pk.class, metadata);
    }

}

