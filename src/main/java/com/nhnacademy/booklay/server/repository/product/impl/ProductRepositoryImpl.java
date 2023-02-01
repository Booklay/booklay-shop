package com.nhnacademy.booklay.server.repository.product.impl;

import com.nhnacademy.booklay.server.dto.member.reponse.MemberForAuthorResponse;
import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveBookForSubscribeResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductBookResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductSubscribeResponse;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductDetail;
import com.nhnacademy.booklay.server.entity.QAuthor;
import com.nhnacademy.booklay.server.entity.QCategory;
import com.nhnacademy.booklay.server.entity.QCategoryProduct;
import com.nhnacademy.booklay.server.entity.QProduct;
import com.nhnacademy.booklay.server.entity.QProductAuthor;
import com.nhnacademy.booklay.server.entity.QProductDetail;
import com.nhnacademy.booklay.server.entity.QProductTag;
import com.nhnacademy.booklay.server.entity.QSubscribe;
import com.nhnacademy.booklay.server.entity.QTag;
import com.nhnacademy.booklay.server.repository.product.ProductRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

/**
 * author: 최규태
 */
public class ProductRepositoryImpl extends QuerydslRepositorySupport implements
    ProductRepositoryCustom {

    public ProductRepositoryImpl() {
        super(Product.class);
    }

    // 수정용 책 조회
    @Override
    public RetrieveProductBookResponse findProductBookDataByProductId(Long id) {
        QProduct product = QProduct.product;
        QProductDetail productDetail = QProductDetail.productDetail;

        return from(product)
            .innerJoin(productDetail).on(productDetail.product.id.eq(product.id))
            .where(product.id.eq(id))
            .select(Projections.constructor(RetrieveProductBookResponse.class,
                                            product.id,
                                            product.title,
                                            product.price,
                                            product.pointRate,
                                            product.shortDescription,
                                            product.longDescription,
                                            product.isSelling,
                                            product.pointMethod,
                                            product.createdAt,
                                            productDetail.id,
                                            productDetail.isbn,
                                            productDetail.page,
                                            productDetail.publisher,
                                            productDetail.publishedDate,
                                            productDetail.ebookAddress,
                                            productDetail.storage
            )).fetchOne();
    }

    // 수정용 구독 조회
    @Override
    public RetrieveProductSubscribeResponse findProductSubscribeDataByProductId(Long id) {
        QProduct product = QProduct.product;
        QSubscribe subscribe = QSubscribe.subscribe;

        return from(product)
            .innerJoin(subscribe).on(subscribe.product.id.eq(product.id))
            .where(product.id.eq(id))
            .select(Projections.constructor(RetrieveProductSubscribeResponse.class,
                                            product.id,
                                            product.title,
                                            product.price,
                                            product.pointRate,
                                            product.shortDescription,
                                            product.longDescription,
                                            product.isSelling,
                                            product.pointMethod,
                                            product.createdAt,
                                            subscribe.id,
                                            subscribe.subscribeWeek,
                                            subscribe.subscribeDay,
                                            subscribe.publisher
            )).fetchOne();
    }

    // 상품 카테고리 조회
    @Override
    public List<Long> findCategoryIdsByProductId(Long id) {
        QProduct product = QProduct.product;
        QCategoryProduct categoryProduct = QCategoryProduct.categoryProduct;
        QCategory category = QCategory.category;

        return from(product)
            .innerJoin(categoryProduct).on(categoryProduct.product.id.eq(product.id))
            .innerJoin(category).on(categoryProduct.category.id.eq(category.id))
            .where(product.id.eq(id))
            .select(category.id)
            .fetch();
    }

    @Override
    public Page<RetrieveBookForSubscribeResponse> findAllBooksForSubscribeBy(Pageable pageable) {
        QProduct product = QProduct.product;
        QProductDetail productDetail = QProductDetail.productDetail;

        List<RetrieveBookForSubscribeResponse> content = from(product)
            .innerJoin(productDetail).on(productDetail.product.id.eq(product.id))
            .select(Projections.constructor(RetrieveBookForSubscribeResponse.class,
                                            product.id,
                                            product.title,
                                            productDetail.publisher
            ))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPQLQuery<Long> count = from(product)
            .select(product.count());
        return PageableExecutionUtils.getPage(content, pageable, count::fetchFirst);
    }

    public List<String> findAuthorNameByProductId(Long productId) {

        QProduct product = QProduct.product;
        QProductAuthor productAuthor = QProductAuthor.productAuthor;
        QProductDetail productDetail = QProductDetail.productDetail;
        QAuthor author = QAuthor.author;
        return from(product)
            .innerJoin(productDetail).on(productDetail.product.id.eq(product.id))
            .innerJoin(productAuthor).on(productAuthor.productDetail.id.eq(productDetail.id))
            .innerJoin(author).on(productAuthor.author.authorId.eq(author.authorId))
            .where(product.id.eq(productId))
            .select(author.name)
            .fetch();
    }

    @Override
    public Page<RetrieveProductResponse> findProductPageByIds(List<Long> ids, Pageable pageable) {

        QProduct product = QProduct.product;

        QCategory category = QCategory.category;
        QCategoryProduct categoryProduct = QCategoryProduct.categoryProduct;

        QSubscribe subscribe = QSubscribe.subscribe;

        QProductDetail productDetail = QProductDetail.productDetail;
        QProductAuthor productAuthor = QProductAuthor.productAuthor;
        QAuthor author = QAuthor.author;

        QProductTag productTag = QProductTag.productTag;
        QTag tag = QTag.tag;

        /**
         * select * from product as pr
         *     inner join category_product cp using (product_no)
         *         inner join category c using (category_no)
         * <p>
         *     left join subscribe using (product_no)
         * <p>
         *     left join product_detail using (product_no)
         *         left join product_author using (book_no)
         *             left join author using (author_no)
         * <p>
         *     left join product_tag using (product_no)
         *         left join tag using (tag_no)
         */

        List<ProductDetail> details =
            from(productDetail)
                .leftJoin(productAuthor).on(productDetail.id.eq(productAuthor.pk.bookId))
                    .leftJoin(author).on(productAuthor.author.authorId.eq(author.authorId))
                .where(productDetail.product.id.in(ids))
                .fetch();

        Map<Long, List<RetrieveAuthorResponse>> listMap = new HashMap<>();

        details.forEach(detail ->
            listMap.put(detail.getProduct().getId(), new ArrayList<>())
        );

        details.forEach(detail ->
            detail.getProductAuthors().forEach( productAuthor1->
                listMap.get(detail.getProduct().getId()).add(
                    new RetrieveAuthorResponse(
                        productAuthor1.getAuthor().getAuthorId(),
                        productAuthor1.getAuthor().getName(),

                        Objects.nonNull(productAuthor1.getAuthor().getMember()) ?
                        new MemberForAuthorResponse(productAuthor1.getAuthor().getMember().getMemberNo(),
                            productAuthor1.getAuthor().getMember().getMemberId()):null
                    ) ))
        );

        List<RetrieveProductResponse> list =
            from(product)
                .innerJoin(categoryProduct).on(product.id.eq(categoryProduct.pk.productId))
                .innerJoin(category).on(categoryProduct.pk.categoryId.eq(category.id))

                .leftJoin(subscribe).on(product.id.eq(subscribe.product.id))

                .leftJoin(productDetail).on(product.id.eq(productDetail.product.id))
                .leftJoin(productAuthor).on(productDetail.product.id.eq(productAuthor.pk.bookId))
                .leftJoin(author).on(productAuthor.author.authorId.eq(author.authorId))

                .leftJoin(productTag).on(product.id.eq(productTag.product.id))
                .leftJoin(tag).on(productTag.tag.id.eq(tag.id))

                .where(product.id.in(ids))
                .select(
                    Projections.constructor(
                        RetrieveProductResponse.class,
                        product,
                        productDetail,
                        subscribe
                    )
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        list.forEach(response -> response.setAuthors(listMap.get(response.getProductId())));

        JPQLQuery<Long> count = from(product)
            .innerJoin(productDetail)
            .on(product.id.eq(productDetail.product.id))
            .select(product.count());

        return PageableExecutionUtils.getPage(list, pageable, count::fetchFirst);
    }
}
