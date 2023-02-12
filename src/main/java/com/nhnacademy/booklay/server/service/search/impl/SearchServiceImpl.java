package com.nhnacademy.booklay.server.service.search.impl;

import com.nhnacademy.booklay.server.dto.product.response.ProductAllInOneResponse;
import com.nhnacademy.booklay.server.dto.search.request.SearchKeywordsRequest;
import com.nhnacademy.booklay.server.dto.search.response.SearchPageResponse;
import com.nhnacademy.booklay.server.dto.search.response.SearchProductResponse;
import com.nhnacademy.booklay.server.entity.Category;
import com.nhnacademy.booklay.server.entity.document.CategoryDocument;
import com.nhnacademy.booklay.server.entity.document.ProductDocument;
import com.nhnacademy.booklay.server.repository.category.CategoryRepository;
import com.nhnacademy.booklay.server.repository.documents.CategoryDocumentRepository;
import com.nhnacademy.booklay.server.repository.documents.ProductDocumentRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.service.search.SearchService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SearchServiceImpl implements SearchService {

    private final CategoryDocumentRepository categoryDocumentRepository;
    private final ProductDocumentRepository productDocumentRepository;

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ElasticsearchOperations operations;

    private static final String KEYWORDS_TEXT = "keywordText";
    private static final String KEYWORDS_ID = "keywordId";
    private static final String[] PRODUCT_NESTED_PATH = { "categories", "tags", "authors"};


    public SearchServiceImpl(CategoryDocumentRepository categoryDocumentRepository,
                             ProductDocumentRepository productDocumentRepository,
                             CategoryRepository categoryRepository,
                             ProductRepository productRepository,
                             ElasticsearchOperations operations) {
        this.categoryDocumentRepository = categoryDocumentRepository;
        this.productDocumentRepository = productDocumentRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.operations = operations;
    }

    @Override
    public SearchPageResponse<SearchProductResponse> getAllProducts(Pageable pageable) {

        Query query = new NativeSearchQueryBuilder()
            .withQuery(
                QueryBuilders.matchAllQuery()
            ).withPageable(pageable).build();

        loggingQueryInfo(query);

        SearchHits<ProductDocument>
            productDocumentSearchHits = operations.search(query, ProductDocument.class);

        return getSearchPageResponse(pageable, productDocumentSearchHits, "전체 상품");

    }

    @Override
    public SearchPageResponse<SearchProductResponse> searchProductsByKeywords(
        SearchKeywordsRequest searchKeywordsRequest, Pageable pageable) {

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        // 요청 정보에서 전체 키워드와 작가/태그/카테고리 키워드 구분
        if (Arrays.stream(PRODUCT_NESTED_PATH).noneMatch(x -> x.equals(searchKeywordsRequest.getClassification()))) {
            queryBuilder.withQuery(searchProductsByOneField(KEYWORDS_TEXT, searchKeywordsRequest.getKeywords()));
        } else {
            queryBuilder.withQuery(searchProductByNestedField(searchKeywordsRequest));
        }

        NativeSearchQuery query = queryBuilder.withPageable(pageable).build();

        loggingQueryInfo(query);

        SearchHits<ProductDocument>
            productDocumentSearchHits = operations.search(query, ProductDocument.class);

        return getSearchPageResponse(pageable, productDocumentSearchHits, searchKeywordsRequest.getKeywords());
    }

    @Override
    public SearchPageResponse<SearchProductResponse> searchProductsByCategory(Long categoryId,
                                                                Pageable pageable) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        NativeSearchQuery query =
            queryBuilder
                .withQuery(nestedQueryForId("categories", categoryId))
                .withPageable(pageable).build();

        loggingQueryInfo(query);

        SearchHits<ProductDocument>
            productDocumentSearchHits = operations.search(query, ProductDocument.class);

        Optional<Category> category = categoryRepository.findById(categoryId);

        String searchTitle = category.isPresent() ? category.get().getName() : "알 수 없는 카테고리 정보";

        return getSearchPageResponse(pageable, productDocumentSearchHits, searchTitle);
    }

    @Override
    public List<SearchProductResponse> getLatestProducts() {

        Query query = new NativeSearchQueryBuilder()
            .withSorts(
                SortBuilders.fieldSort("createdAt")
            ).withMaxResults(8)
            .build();

        loggingQueryInfo(query);

        SearchHits<ProductDocument>
            productDocumentSearchHits = operations.search(query, ProductDocument.class);

        return convertHitsToResponse(getHits(productDocumentSearchHits));
    }

    @Override
    public void saveAllDocuments() {
//        카테고리 인덱스 저장

        List<Category> categories = categoryRepository.findAll();
        List<CategoryDocument> categoryDocuments = new ArrayList<>();

        if (!categories.isEmpty()) {
            categories.forEach(category ->
                categoryDocuments.add(CategoryDocument.fromCategory(category)));
            categoryDocumentRepository.saveAll(categoryDocuments);
        }

//        상품 인덱스 저장

        List<ProductAllInOneResponse> products = productRepository.retrieveAllProducts();
        List<ProductDocument> productDocuments = new ArrayList<>();

        if (!products.isEmpty()) {
            AtomicInteger count = new AtomicInteger(1);
            products.forEach(product -> {
                productDocuments.add(ProductDocument.fromEntity(product));
                log.info(" Docs Add Counts : {}", count.getAndIncrement());
            });

            productDocumentRepository.saveAll(productDocuments);
        }

    }

    private SearchPageResponse<SearchProductResponse> getSearchPageResponse(Pageable pageable,
                                                                            SearchHits<ProductDocument> productDocumentSearchHits,
                                                                            String searchTitle) {

        List<SearchProductResponse> list = convertHitsToResponse(getHits(productDocumentSearchHits));

        return new SearchPageResponse<>(
            searchTitle,
            pageable.getPageNumber(),
            pageable.getPageSize(),
            list.size() / pageable.getPageSize(),
            list);
    }


    private List<SearchProductResponse> convertHitsToResponse(List<ProductDocument> list) {

        List<SearchProductResponse> responses = new ArrayList<>();

        list.forEach(x -> responses.add(new SearchProductResponse(x)));

        return responses;
    }

    /**
     * 검색된 결과들의 아이디 리스트만 리턴해주는 메소드,
     * 조회한 정보를 MySQL을 통해 페이지를 리턴하기 때문에 ID 리스트만 리턴.
     * @param searchHits 검색된 도큐먼트.
     * @return 아이디 리스트.
     * @param <T> 도큐먼트 타입.
     */
    private <T> List<Long> getHitIds(SearchHits<T> searchHits) {
        List<Long> hitIds = new ArrayList<>();

        searchHits.getSearchHits().forEach(
            productDocumentSearchHit -> hitIds.add(
                Long.valueOf(Objects.requireNonNull(productDocumentSearchHit.getId()))));

        return hitIds;
    }

    private <T> List<T> getHits(SearchHits<T> searchHits) {
        List<T> hits = new ArrayList<>();

        if (Objects.nonNull(searchHits)) {
            searchHits.getSearchHits().forEach(
                productDocumentSearchHit -> hits.add(productDocumentSearchHit.getContent()));
        }


        return hits;
    }

    private MatchQueryBuilder searchProductsByOneField(String field, String keywords) {
        return QueryBuilders.matchQuery(field, keywords);
    }

    private NestedQueryBuilder nestedQuery(String nestedPath, String field, String text) {
        return QueryBuilders.nestedQuery(
            nestedPath,
            QueryBuilders.matchQuery(field, text),
            ScoreMode.None);
    }

    private NestedQueryBuilder searchProductByNestedField(
        SearchKeywordsRequest searchKeywordsRequest) {
        return nestedQuery(
            searchKeywordsRequest.getClassification(),
            searchKeywordsRequest.getClassification() + ".name",
            searchKeywordsRequest.getKeywords());
    }

    private NestedQueryBuilder nestedQueryForId(String path, Long id) {
        return nestedQuery(path, path + ".id", String.valueOf(id));
    }

    private static void loggingQueryInfo(Query query) {
        log.debug(" \n Query : \n {}", ((NativeSearchQuery) query).getQuery());
    }
}
