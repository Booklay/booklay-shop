package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.dto.member.reponse.MemberForAuthorResponse;
import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.entity.ProductAuthor;
import com.nhnacademy.booklay.server.entity.QAuthor;
import com.nhnacademy.booklay.server.entity.QProductAuthor;
import com.nhnacademy.booklay.server.entity.QProductDetail;
import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class ProductDetailRepositoryImpl extends QuerydslRepositorySupport implements ProductDetailRepositoryCustom {

  public ProductDetailRepositoryImpl() {
    super(ProductAuthor.class);
  }

  public List<RetrieveAuthorResponse> findAuthorsByProductDetailId(Long id){
    QProductDetail productDetail = QProductDetail.productDetail;
    QProductAuthor productAuthor = QProductAuthor.productAuthor;
    QAuthor author = QAuthor.author;

    return from(productDetail)
        .innerJoin(productAuthor).on(productDetail.id.eq(productAuthor.productDetail.id))
        .innerJoin(author).on(productAuthor.author.authorNo.eq(author.authorNo))
        .where(productDetail.id.eq(id))
        .select(Projections.constructor(RetrieveAuthorResponse.class, author.authorNo, author.name, Projections.constructor(
            MemberForAuthorResponse.class, author.member.memberNo, author.member.memberId))).fetch();
  }
}
