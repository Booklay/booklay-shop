package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.dto.member.reponse.MemberForAuthorResponse;
import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductBookResponse;
import com.nhnacademy.booklay.server.entity.ProductAuthor;
import com.nhnacademy.booklay.server.entity.QAuthor;
import com.nhnacademy.booklay.server.entity.QCategory;
import com.nhnacademy.booklay.server.entity.QCategoryProduct;
import com.nhnacademy.booklay.server.entity.QMember;
import com.nhnacademy.booklay.server.entity.QProduct;
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
    QMember member = QMember.member;

    return from(productDetail)
        .innerJoin(productAuthor).on(productDetail.id.eq(productAuthor.productDetail.id))
        .innerJoin(author).on(productAuthor.author.authorId.eq(author.authorId))
        .leftJoin(member).on(author.member.memberNo.eq(member.memberNo))
        .where(productDetail.id.eq(id))
        .select(Projections.constructor(RetrieveAuthorResponse.class, author.authorId, author.name
            ,(Projections.constructor(MemberForAuthorResponse.class ,member.memberNo, member.memberId))
        )).fetch();
  }

  @Override
  public RetrieveProductBookResponse findProductBookDateByDetailId(Long id) {
    QProductDetail productDetail = QProductDetail.productDetail;
    QProduct product = QProduct.product;
    QProductAuthor productAuthor = QProductAuthor.productAuthor;
    QAuthor author = QAuthor.author;
    QCategoryProduct categoryProduct = QCategoryProduct.categoryProduct;
    QCategory category = QCategory.category;

    return from(productDetail)
        .innerJoin(product).on(productDetail.product.id.eq(product.id))
        .where(productDetail.id.eq(id))
        .select(Projections.constructor(RetrieveProductBookResponse.class,
            product.id,
            product.title,
            product.price,
            product.pointRate,
            product.shortDescription,
            product.longDescription,
            product.isSelling,
            product.pointMethod,
            productDetail.id,
            productDetail.isbn,
            productDetail.page,
            productDetail.publisher,
            productDetail.publishedDate,
            productDetail.ebookAddress,
            productDetail.storage,
            from(productDetail).innerJoin(productAuthor).on(productDetail.id.eq(productAuthor.productDetail.id))
                .innerJoin(author).on(productAuthor.author.authorId.eq(author.authorId))
                .where(productDetail.id.eq(id))
                .select(author.authorId),
            from(productDetail).innerJoin(product).on(productDetail.product.id.eq(product.id))
                .innerJoin(categoryProduct).on(product.id.eq(categoryProduct.product.id))
                .innerJoin(category).on(categoryProduct.category.id.eq(category.id))
                .where(productDetail.id.eq(id))
                .select(category.id)
        )).fetchOne();
  }
}
