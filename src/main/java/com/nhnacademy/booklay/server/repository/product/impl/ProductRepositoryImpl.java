package com.nhnacademy.booklay.server.repository.product.impl;

import com.nhnacademy.booklay.server.dto.category.response.CategoryResponse;
import com.nhnacademy.booklay.server.dto.product.author.AuthorsInProduct;
import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveBookForSubscribeResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductBookResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductSubscribeResponse;
import com.nhnacademy.booklay.server.dto.product.tag.response.RetrieveTagResponse;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.QAuthor;
import com.nhnacademy.booklay.server.entity.QCategory;
import com.nhnacademy.booklay.server.entity.QCategoryProduct;
import com.nhnacademy.booklay.server.entity.QMember;
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
import org.springframework.data.domain.Page;
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

        QSubscribe subscribe = QSubscribe.subscribe;
        QProductDetail productDetail = QProductDetail.productDetail;

        QProductAuthor productAuthor = QProductAuthor.productAuthor;
        QAuthor author = QAuthor.author;

        List<AuthorsInProduct> authorList =
            from(productDetail)
                .leftJoin(productAuthor).on(productAuthor.productDetail.id.eq(productDetail.id))
                    .leftJoin(author).on(author.authorId.eq(productAuthor.author.authorId))
                .where(productDetail.product.id.in(ids))
                .select(Projections.constructor(
                    AuthorsInProduct.class, productDetail.product.id, author))
                .fetch();

        Map<Long, List<RetrieveAuthorResponse>> authorsInProduct = new HashMap<>();
        authorList.forEach(x -> authorsInProduct.put(x.getProductId(), new ArrayList<>()));
        authorList.forEach(x -> authorsInProduct.get(x.getProductId()).add(x.getAuthor()));

        List<RetrieveProductResponse> list =
            from(product)
                .leftJoin(subscribe).on(product.id.eq(subscribe.product.id))
                .leftJoin(productDetail).on(product.id.eq(productDetail.product.id))
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

        list.forEach(x -> x.setAuthors(authorsInProduct.get(x.getProductId())));

        JPQLQuery<Long> count = from(product)
            .innerJoin(productDetail)
            .on(product.id.eq(productDetail.product.id))
            .select(product.count());

        return PageableExecutionUtils.getPage(list, pageable, count::fetchFirst);
    }

    @Override
    public RetrieveProductSubscribeResponse retrieveProductSubscribeResponseById(Long id) {

        QProduct product = QProduct.product;

        QSubscribe subscribe = QSubscribe.subscribe;

        List<CategoryResponse> categories =
            getCategoryResponseListByProductId(id);

        List<RetrieveTagResponse> tags =
            getTagResponsesByProductId(id, product);

        RetrieveProductSubscribeResponse productResponse =
            from(product)
                .leftJoin(subscribe).on(product.id.eq(subscribe.product.id))
                .where(product.id.in(id))
                .select(Projections.constructor(RetrieveProductSubscribeResponse.class, product, subscribe))
                .fetchOne();

        productResponse.setCategoryResponseList(categories);
        productResponse.setTagResponseList(tags);

        return productResponse;
    }

    @Override
    public RetrieveProductBookResponse retrieveProductBookResponse(Long id) {
        QProduct product = QProduct.product;

        QProductDetail productDetail = QProductDetail.productDetail;

        List<RetrieveAuthorResponse>
            authors = getAuthorResponsesByProductId(id, productDetail);

        List<CategoryResponse> categories =
            getCategoryResponseListByProductId(id);

        List<RetrieveTagResponse> tags =
            getTagResponsesByProductId(id, product);

        RetrieveProductBookResponse productResponse =
            from(product)
                .leftJoin(productDetail).on(product.id.eq(productDetail.product.id))
                .where(product.id.in(id))
                .select(Projections.constructor(RetrieveProductBookResponse.class, product, productDetail))
                .fetchOne();

        productResponse.setCategoryResponseList(categories);
        productResponse.setTagResponseList(tags);
        productResponse.setAuthorResponses(authors);

        return productResponse;
    }

    private List<RetrieveAuthorResponse> getAuthorResponsesByProductId(Long id, QProductDetail productDetail) {
        QProductAuthor productAuthor = QProductAuthor.productAuthor;
        QAuthor author = QAuthor.author;
        QMember member = QMember.member;

        return from(author)
            .leftJoin(productAuthor).on(productAuthor.productDetail.id.eq(productDetail.id))
            .leftJoin(member).on(member.memberNo.eq(author.member.memberNo))
            .where(productDetail.product.id.eq(id))
            .select(Projections.constructor(RetrieveAuthorResponse.class, author.authorId, author.name,
                member))
            .fetch();
    }

    private List<RetrieveTagResponse> getTagResponsesByProductId(Long id, QProduct product) {
        QProductTag productTag = QProductTag.productTag;
        QTag tag = QTag.tag;

        return from(tag)
            .leftJoin(productTag).on(product.id.eq(productTag.product.id))
            .where(productTag.product.id.eq(id))
            .select(Projections.constructor(RetrieveTagResponse.class, tag))
            .fetch();
    }

    private List<CategoryResponse> getCategoryResponseListByProductId(Long id) {

        QCategory category = QCategory.category;
        QCategoryProduct categoryProduct = QCategoryProduct.categoryProduct;

        return from(category)
                .leftJoin(categoryProduct).on(categoryProduct.category.id.eq(category.id))
                .where(categoryProduct.product.id.eq(id))
                .select(Projections.constructor(CategoryResponse.class, category))
                .fetch();
    }
}


