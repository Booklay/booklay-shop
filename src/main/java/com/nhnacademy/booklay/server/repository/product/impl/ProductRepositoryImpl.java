package com.nhnacademy.booklay.server.repository.product.impl;

import com.nhnacademy.booklay.server.dto.category.response.CategoryResponse;
import com.nhnacademy.booklay.server.dto.product.author.AuthorsInProduct;
import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.dto.product.category.CategoriesInProduct;
import com.nhnacademy.booklay.server.dto.product.response.ProductAllInOneResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveBookForSubscribeResponse;
import com.nhnacademy.booklay.server.dto.product.tag.TagsInProduct;
import com.nhnacademy.booklay.server.dto.product.tag.response.RetrieveTagResponse;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.QAuthor;
import com.nhnacademy.booklay.server.entity.QCategory;
import com.nhnacademy.booklay.server.entity.QCategoryProduct;
import com.nhnacademy.booklay.server.entity.QProduct;
import com.nhnacademy.booklay.server.entity.QProductAuthor;
import com.nhnacademy.booklay.server.entity.QProductDetail;
import com.nhnacademy.booklay.server.entity.QProductTag;
import com.nhnacademy.booklay.server.entity.QSubscribe;
import com.nhnacademy.booklay.server.entity.QTag;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.repository.product.ProductRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

/**
 * author: 최규태
 */

@Slf4j
public class ProductRepositoryImpl extends QuerydslRepositorySupport implements
    ProductRepositoryCustom {

    public ProductRepositoryImpl() {
        super(Product.class);
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
    public Page<Product> findNotDeletedByPageable(Pageable pageable) {
        QProduct product = QProduct.product;

        List<Product> productList = from(product)
            .where(product.isDeleted.eq(false))
            .select(product)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPQLQuery<Long> count = from(product)
            .select(product.count());
        return PageableExecutionUtils.getPage(productList, pageable, count::fetchFirst);
    }

    /**
     * 전체 상품에 대한 정보.
     */
    @Override
    public List<ProductAllInOneResponse> retrieveAllProducts() {
        return retrieveProductsByCondition(List.of(), Pageable.unpaged());
    }

    /**
     * 페이지네이션을 적용한 전체 상품 정보.
     */
    @Override
    public Page<ProductAllInOneResponse> retrieveProductsInPage(Pageable pageable) {

        QProduct product = QProduct.product;

        JPQLQuery<Long> count = from(product)
            .select(product.count());

        List<ProductAllInOneResponse> productList = retrieveProductsByCondition(List.of(), pageable);

        return PageableExecutionUtils.getPage(productList, pageable, count::fetchFirst);
    }

    /**
     * 상품 번호 리스트를 조건으로 검색
     */
    @Override
    public List<ProductAllInOneResponse> retrieveProductsByIds(List<Long> ids) {
        return retrieveProductsByCondition(ids, Pageable.unpaged());
    }

    /**
     * 상품 번호 리스트를 조건으로 검색하고 페이지네이션 적용 정보.
     */
    @Override
    public Page<ProductAllInOneResponse> retrieveProductsByIdsInPage(List<Long> ids, Pageable pageable) {

        QProduct product = QProduct.product;

        JPQLQuery<Long> count = from(product)
            .select(product.count());

        List<ProductAllInOneResponse> productList = retrieveProductsByCondition(ids, pageable);

        return PageableExecutionUtils.getPage(productList, pageable, count::fetchFirst);
    }

    /**
     * 상품 번호를 조건으로 하나의 상품만 검색
     */
    @Override
    public ProductAllInOneResponse retrieveProductById(Long id) {

        log.info("요청된 번호 : {}", id);

        return retrieveProductsByCondition(List.of(id), Pageable.unpaged())
            .stream()
            .findFirst()
            .orElseThrow(() -> new NotFoundException(Product.class, "존재하지 않는 상품 번호입니다."));
    }

    /**
     * 상품에 대한 조인 쿼리들을 매개변수에 따라 쿼리를 추가하고 fetch() 후에 리턴하는 메소드.
     */
    @Override
    public List<ProductAllInOneResponse> retrieveProductsByCondition(List<Long> ids, Pageable pageable) {

        QProduct product = QProduct.product;
        QProductTag productTag = QProductTag.productTag;
        QProductDetail productDetail = QProductDetail.productDetail;
        QCategoryProduct categoryProduct = QCategoryProduct.categoryProduct;

        JPQLQuery<ProductAllInOneResponse> productResponseQuery = getProductResponseList(product);

        JPQLQuery<TagsInProduct> tagsInProductQuery = getTagInProductList();
        JPQLQuery<AuthorsInProduct> authorsInProductQuery = getAuthorsInProductList();
        JPQLQuery<CategoriesInProduct> categoriesInProductQuery = getCategoriesInProductList();

        // 검색을 위한 ID 리스트가 비어있지 않다면
        if (!ids.isEmpty()) {
            tagsInProductQuery = tagsInProductQuery.where(productTag.product.id.in(ids));
            authorsInProductQuery = authorsInProductQuery.where(productDetail.product.id.in(ids));
            categoriesInProductQuery = categoriesInProductQuery.where(categoryProduct.product.id.in(ids));
            productResponseQuery = productResponseQuery.where(product.id.in(ids));
        }

        // 페이지 정보가 존재하면
        if (pageable.isPaged()) {
            productResponseQuery = productResponseQuery
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        }

        List<TagsInProduct> tagsInProductList = tagsInProductQuery.fetch();
        List<AuthorsInProduct> authorsInProductList = authorsInProductQuery.fetch();
        List<CategoriesInProduct> categoriesInProductList = categoriesInProductQuery.fetch();

        List<ProductAllInOneResponse> productList = productResponseQuery.fetch();

        setListByProductId(authorsInProductList, tagsInProductList, categoriesInProductList, productList);

        return productList;

    }

    /**
     * 상품과 구독/책 정보 조인 쿼리.
     */

    private JPQLQuery<ProductAllInOneResponse> getProductResponseList(QProduct product) {

        QSubscribe subscribe = QSubscribe.subscribe;
        QProductDetail productDetail = QProductDetail.productDetail;

        return from(product)
            .leftJoin(subscribe).on(product.id.eq(subscribe.product.id))
            .leftJoin(productDetail).on(product.id.eq(productDetail.product.id))
            .select(
                Projections.constructor(
                    ProductAllInOneResponse.class,
                    product,
                    productDetail,
                    subscribe
                )
            );
    }

    /**
     * 상품과 태그 조인 쿼리.
     */
    private JPQLQuery<TagsInProduct> getTagInProductList(){
        QProductTag productTag = QProductTag.productTag;
        QTag tag = QTag.tag;

        return from(productTag)
            .leftJoin(tag).on(productTag.tag.id.eq(tag.id))
            .select(Projections.constructor(TagsInProduct.class, productTag.product.id, tag));
    }

    /**
     * 상품과 작가 정보 조인 쿼리(구독에 대한 작가는 존재하지 않음).
     */
    private JPQLQuery<AuthorsInProduct> getAuthorsInProductList() {

        QProductDetail productDetail = QProductDetail.productDetail;
        QProductAuthor productAuthor = QProductAuthor.productAuthor;
        QAuthor author = QAuthor.author;

        return from(productDetail)
            .leftJoin(productAuthor).on(productAuthor.productDetail.id.eq(productDetail.id))
            .leftJoin(author).on(author.authorId.eq(productAuthor.author.authorId))
            .select(Projections.constructor(AuthorsInProduct.class, productDetail.product.id, author));
    }

    /**
     * 상품과 카테고리 조인 쿼리.
     */
    private JPQLQuery<CategoriesInProduct> getCategoriesInProductList() {

        QCategory category = QCategory.category;
        QCategoryProduct categoryProduct = QCategoryProduct.categoryProduct;

        return from(category)
            .leftJoin(categoryProduct).on(categoryProduct.category.id.eq(category.id))
            .select(Projections.constructor(CategoriesInProduct.class, categoryProduct.product.id, category));
    }

    /**
     * 카테고리, 태그, 작가 리스트를 상품 아이디와 매칭.
     */
    private void setListByProductId(List<AuthorsInProduct> authorList,
                                    List<TagsInProduct> tagList,
                                    List<CategoriesInProduct> categories,
                                    List<ProductAllInOneResponse> list) {

        Map<Long, List<RetrieveTagResponse>> tagMap = new HashMap<>();
        tagList.forEach(x -> tagMap.put(x.getProductId(), new ArrayList<>()));
        tagList.forEach(x -> tagMap.get(x.getProductId()).add(x.getTag()));

        Map<Long, List<RetrieveAuthorResponse>> authorMap = new HashMap<>();
        authorList.forEach(x -> authorMap.put(x.getProductId(), new ArrayList<>()));
        authorList.forEach(x -> authorMap.get(x.getProductId()).add(x.getAuthor()));

        Map<Long, List<CategoryResponse>> categoryMap = new HashMap<>();
        categories.forEach(x -> categoryMap.put(x.getProductId(), new ArrayList<>()));
        categories.forEach(x -> categoryMap.get(x.getProductId()).add(x.getCategory()));

        list.forEach(x -> {
            x.setTags(tagMap.getOrDefault(x.getInfo().getId(), new ArrayList<>()));
            x.setAuthors(authorMap.getOrDefault(x.getInfo().getId(), new ArrayList<>()));
            x.setCategories(categoryMap.getOrDefault(x.getInfo().getId(), new ArrayList<>()));
        });
    }
}


