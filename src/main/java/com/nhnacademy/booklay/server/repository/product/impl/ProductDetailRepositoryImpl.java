package com.nhnacademy.booklay.server.repository.product.impl;

import com.nhnacademy.booklay.server.dto.member.response.MemberForAuthorResponse;
import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.entity.ProductAuthor;
import com.nhnacademy.booklay.server.entity.QAuthor;
import com.nhnacademy.booklay.server.entity.QMember;
import com.nhnacademy.booklay.server.entity.QProductAuthor;
import com.nhnacademy.booklay.server.entity.QProductDetail;
import com.nhnacademy.booklay.server.repository.product.ProductDetailRepositoryCustom;
import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ProductDetailRepositoryImpl extends QuerydslRepositorySupport implements
    ProductDetailRepositoryCustom {

    public ProductDetailRepositoryImpl() {
        super(ProductAuthor.class);
    }

    public List<RetrieveAuthorResponse> findAuthorsByProductDetailId(Long id) {
        QProductDetail productDetail = QProductDetail.productDetail;
        QProductAuthor productAuthor = QProductAuthor.productAuthor;
        QAuthor author = QAuthor.author;
        QMember member = QMember.member;

        return from(productDetail)
            .innerJoin(productAuthor).on(productDetail.id.eq(productAuthor.productDetail.id))
            .innerJoin(author).on(productAuthor.author.authorId.eq(author.authorId))
            .leftJoin(member).on(author.member.memberNo.eq(member.memberNo))
            .where(productDetail.id.eq(id))
            .select(
                Projections.constructor(RetrieveAuthorResponse.class, author.authorId, author.name
                    , (Projections.constructor(MemberForAuthorResponse.class, member.memberNo,
                                               member.memberId))
                )).fetch();
    }

    @Override
    public List<Long> findAuthorIdsByProductDetailId(Long id) {
        QProductDetail productDetail = QProductDetail.productDetail;
        QProductAuthor productAuthor = QProductAuthor.productAuthor;
        QAuthor author = QAuthor.author;

        return from(productDetail)
            .innerJoin(productAuthor).on(productDetail.id.eq(productAuthor.productDetail.id))
            .innerJoin(author).on(productAuthor.author.authorId.eq(author.authorId))
            .where(productDetail.id.eq(id))
            .select(author.authorId)
            .fetch();
    }

}
