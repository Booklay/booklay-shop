package com.nhnacademy.booklay.server.repository.product.impl;

import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.category.response.CategoryResponse;
import com.nhnacademy.booklay.server.dto.product.author.AuthorsInProduct;
import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.dto.product.category.CategoriesInProduct;
import com.nhnacademy.booklay.server.dto.product.response.ProductAllInOneResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveBookForSubscribeResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductBookResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductSubscribeResponse;
import com.nhnacademy.booklay.server.dto.product.tag.TagsInProduct;
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

    @Deprecated(forRemoval = true)
    public ProductRepositoryImpl() {
        super(Product.class);
    }

    // 수정용 책 조회

    @Deprecated(forRemoval = true)
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
    @Deprecated(forRemoval = true)
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
    @Deprecated(forRemoval = true)
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


    @Deprecated(forRemoval = true)
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

        productResponse.setCategoryList(categories);
        productResponse.setTagList(tags);

        return productResponse;
    }


    @Deprecated(forRemoval = true)
    @Override
    public RetrieveProductBookResponse retrieveProductBookResponse(Long id) {
        QProduct product = QProduct.product;

        QProductDetail productDetail = QProductDetail.productDetail;

        List<CategoryResponse> categories =
            getCategoryResponseListByProductId(id);

        List<RetrieveTagResponse> tags =
            getTagResponsesByProductId(id, product);

        List<RetrieveAuthorResponse> authors = getAuthorResponsesByProductId(id, productDetail);

        RetrieveProductBookResponse productResponse =
            from(product)
                .leftJoin(productDetail).on(product.id.eq(productDetail.product.id))
                .where(product.id.in(id))
                .select(Projections.constructor(RetrieveProductBookResponse.class, product, productDetail))
                .fetchOne();

        productResponse.setCategoryList(categories);
        productResponse.setTagList(tags);
        productResponse.setAuthorList(authors);

        return productResponse;
    }

    @Deprecated(forRemoval = true)
    private List<RetrieveAuthorResponse> getAuthorResponsesByProductId(Long id, QProductDetail productDetail) {
        QProductAuthor productAuthor = QProductAuthor.productAuthor;
        QAuthor author = QAuthor.author;
        QMember member = QMember.member;

        return from(author)
            .leftJoin(member).on(author.member.memberNo.eq(member.memberNo))
            .leftJoin(productAuthor).on(author.authorId.eq(productAuthor.author.authorId))
            .leftJoin(productDetail).on(productAuthor.productDetail.id.eq(productDetail.id))
            .where(productDetail.product.id.eq(id))
            .select(Projections.constructor(RetrieveAuthorResponse.class, author, member))
            .fetch();
    }

    @Deprecated(forRemoval = true)
    private List<RetrieveTagResponse> getTagResponsesByProductId(Long id, QProduct product) {
        QProductTag productTag = QProductTag.productTag;
        QTag tag = QTag.tag;

        return from(product)
            .leftJoin(productTag).on(product.id.eq(productTag.product.id))
            .leftJoin(tag).on(productTag.tag.id.eq(tag.id))
            .where(productTag.product.id.eq(id))
            .select(Projections.constructor(RetrieveTagResponse.class, tag))
            .fetch();
    }

    @Deprecated(forRemoval = true)
    private List<CategoryResponse> getCategoryResponseListByProductId(Long id) {

        QCategory category = QCategory.category;
        QCategoryProduct categoryProduct = QCategoryProduct.categoryProduct;

        return from(category)
                .leftJoin(categoryProduct).on(categoryProduct.category.id.eq(category.id))
                .where(categoryProduct.product.id.eq(id))
                .select(Projections.constructor(CategoryResponse.class, category))
                .fetch();
    }

    /**
     * 상품 하나에 대하여 정보 획득, List를 조건으로 사용하는 메소드에 List.of( {ONE_ELEMENT} ) 정보를 주어 하나의 상품 정보만 가져옴.
     * 페이지를 리턴하는 메소드이기 때문에 .getData() 의 첫번째 객체를 반환
     */

    //TODO 검색 결과가 존재하지 않을 때, 예외처리 고려

    @Override
    public ProductAllInOneResponse retrieveProductResponse(Long id) {
        Page<ProductAllInOneResponse> page = retrieveProductPage(List.of(id), Pageable.ofSize(1));

        PageResponse<ProductAllInOneResponse> pageResponse = new PageResponse<>(page);

        return pageResponse.getData().get(0);
    }

    /**
     * 상품 ID 리스트와 페이지네이션을 조건으로 검색된 상품 페이지 리턴.
     */
    @Override
    public Page<ProductAllInOneResponse> retrieveProductPage(List<Long> ids, Pageable pageable) {

        QProduct product = QProduct.product;

        Map<Long, List<RetrieveAuthorResponse>> authorsInProductList = getAuthorsInProductList(ids);
        Map<Long, List<RetrieveTagResponse>> tagsInProductList = getTagsInProductList(ids);
        Map<Long, List<CategoryResponse>> categoriesInProductList = getCategoriesInProductList(ids);

        List<ProductAllInOneResponse> list = getRetrieveProductResponseList(ids, pageable, product);

        list.forEach(x -> x.setAuthors(authorsInProductList.get(x.getInfo().getId())));
        list.forEach(x -> x.setTags(tagsInProductList.get(x.getInfo().getId())));
        list.forEach(x -> x.setCategories(categoriesInProductList.get(x.getInfo().getId())));

        JPQLQuery<Long> count = from(product)
            .select(product.count());

        return PageableExecutionUtils.getPage(list, pageable, count::fetchFirst);
    }

    /**
     * ID 리스트 검색 조건에 대해 상품 기본 정보 획득.
     */

    private List<ProductAllInOneResponse> getRetrieveProductResponseList(List<Long> ids, Pageable pageable, QProduct product) {

        QSubscribe subscribe = QSubscribe.subscribe;
        QProductDetail productDetail = QProductDetail.productDetail;

        return from(product)
            .leftJoin(subscribe).on(product.id.eq(subscribe.product.id))
            .leftJoin(productDetail).on(product.id.eq(productDetail.product.id))
            .where(product.id.in(ids))
            .select(
                Projections.constructor(
                    ProductAllInOneResponse.class,
                    product,
                    productDetail,
                    subscribe
                )
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    /**
     *  상품 ID 리스트를 조건으로 태그, 카테고리, 작가 리스트 리턴
     */

    private Map<Long, List<RetrieveTagResponse>> getTagsInProductList(List<Long> ids) {

        QProductTag productTag = QProductTag.productTag;
        QTag tag = QTag.tag;

        List<TagsInProduct> tagList = from(productTag)
            .leftJoin(tag).on(productTag.tag.id.eq(tag.id))
            .where(productTag.product.id.in(ids))
            .select(Projections.constructor(TagsInProduct.class, productTag.product.id, tag))
            .fetch();

        return getTagsInProduct(tagList);

    }

    private Map<Long, List<RetrieveAuthorResponse>> getAuthorsInProductList(List<Long> ids) {

        QProductDetail productDetail = QProductDetail.productDetail;
        QProductAuthor productAuthor = QProductAuthor.productAuthor;
        QAuthor author = QAuthor.author;

        List<AuthorsInProduct> authorList = from(productDetail)
            .leftJoin(productAuthor).on(productAuthor.productDetail.id.eq(productDetail.id))
            .leftJoin(author).on(author.authorId.eq(productAuthor.author.authorId))
            .where(productDetail.product.id.in(ids))
            .select(Projections.constructor(
                AuthorsInProduct.class, productDetail.product.id, author))
            .fetch();

        return getAuthorsInProduct(authorList);
    }

    private Map<Long, List<CategoryResponse>> getCategoriesInProductList(List<Long> ids) {

        QCategory category = QCategory.category;
        QCategoryProduct categoryProduct = QCategoryProduct.categoryProduct;

        List<CategoriesInProduct> categories = from(category)
            .leftJoin(categoryProduct).on(categoryProduct.category.id.eq(category.id))
            .where(categoryProduct.product.id.in(ids))
            .select(Projections.constructor(CategoriesInProduct.class, categoryProduct.product.id, category))
            .fetch();

        return getListMap(categories);

    }

    /**
     * 전체 상품에 대한 페이지네이션.
     */
    @Override
    public Page<ProductAllInOneResponse> retrieveProductPage(Pageable pageable) {

        QProduct product = QProduct.product;

        List<ProductAllInOneResponse> list = getRetrieveProductResponseList(pageable, product);

        Map<Long, List<RetrieveAuthorResponse>> authorsInProductList = getAuthorsInProductList();
        Map<Long, List<RetrieveTagResponse>> tagsInProductList = getTagsInProductList();
        Map<Long, List<CategoryResponse>> categoriesInProductList = getCategoriesInProductList();


        setListByProductId(authorsInProductList, tagsInProductList, categoriesInProductList, list);

        JPQLQuery<Long> count = from(product)
            .select(product.count());

        return PageableExecutionUtils.getPage(list, pageable, count::fetchFirst);
    }

    /**
     * 전체 상품에 대해 페이지네이션만 적용하여 기본 상품 정보 리턴.
     */

    private List<ProductAllInOneResponse> getRetrieveProductResponseList(Pageable pageable, QProduct product) {

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
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }


    /**
     * 조건 없이 모든 상품에 대해 태그, 카테고리, 작가 정보 리턴.
     */

    private Map<Long, List<RetrieveTagResponse>> getTagsInProductList() {

        QProductTag productTag = QProductTag.productTag;
        QTag tag = QTag.tag;

        List<TagsInProduct> tagList = from(productTag)
            .leftJoin(tag).on(productTag.tag.id.eq(tag.id))
            .select(Projections.constructor(TagsInProduct.class, productTag.product.id, tag))
            .fetch();

        return getTagsInProduct(tagList);

    }

    private Map<Long, List<RetrieveAuthorResponse>> getAuthorsInProductList() {

        QProductDetail productDetail = QProductDetail.productDetail;
        QProductAuthor productAuthor = QProductAuthor.productAuthor;
        QAuthor author = QAuthor.author;

        List<AuthorsInProduct> authorList = from(productDetail)
            .leftJoin(productAuthor).on(productAuthor.productDetail.id.eq(productDetail.id))
            .leftJoin(author).on(author.authorId.eq(productAuthor.author.authorId))
            .select(Projections.constructor(
                AuthorsInProduct.class, productDetail.product.id, author))
            .fetch();

        return getAuthorsInProduct(authorList);
    }

    private Map<Long, List<CategoryResponse>> getCategoriesInProductList() {

        QCategory category = QCategory.category;
        QCategoryProduct categoryProduct = QCategoryProduct.categoryProduct;

        List<CategoriesInProduct> categories = from(category)
            .leftJoin(categoryProduct).on(categoryProduct.category.id.eq(category.id))
            .select(Projections.constructor(CategoriesInProduct.class, categoryProduct.product.id, category))
            .fetch();

        return getListMap(categories);

    }

    /**
     * 맵으로 정리된 카테고리, 태그, 작가 리스트를 각 상품 아이디와 매칭.
     */
    private void setListByProductId(Map<Long, List<RetrieveAuthorResponse>> authorsInProductList,
                           Map<Long, List<RetrieveTagResponse>> tagsInProductList,
                           Map<Long, List<CategoryResponse>> categoriesInProductList,
                           List<ProductAllInOneResponse> list) {
        list.forEach(x -> x.setAuthors(authorsInProductList.get(x.getInfo().getId())));
        list.forEach(x -> x.setTags(tagsInProductList.get(x.getInfo().getId())));
        list.forEach(x -> x.setCategories(categoriesInProductList.get(x.getInfo().getId())));
    }

    /**
     * 태그, 작가, 카테고리 리스트를 productId를 키로 가지는 맵으로 정리.
     */
    private Map<Long, List<RetrieveTagResponse>> getTagsInProduct(List<TagsInProduct> tagList) {
        Map<Long, List<RetrieveTagResponse>> tagsInProduct = new HashMap<>();
        tagList.forEach(x -> tagsInProduct.put(x.getProductId(), new ArrayList<>()));
        tagList.forEach(x -> tagsInProduct.get(x.getProductId()).add(x.getTag()));
        return tagsInProduct;
    }

    private Map<Long, List<RetrieveAuthorResponse>> getAuthorsInProduct(List<AuthorsInProduct> authorList) {
        Map<Long, List<RetrieveAuthorResponse>> authorsInProduct = new HashMap<>();
        authorList.forEach(x -> authorsInProduct.put(x.getProductId(), new ArrayList<>()));
        authorList.forEach(x -> authorsInProduct.get(x.getProductId()).add(x.getAuthor()));
        return authorsInProduct;
    }

    private Map<Long, List<CategoryResponse>> getListMap(List<CategoriesInProduct> categories) {
        Map<Long, List<CategoryResponse>> categoriesInProduct = new HashMap<>();
        categories.forEach(x -> categoriesInProduct.put(x.getProductId(), new ArrayList<>()));
        categories.forEach(x -> categoriesInProduct.get(x.getProductId()).add(x.getCategory()));
        return categoriesInProduct;
    }
}


