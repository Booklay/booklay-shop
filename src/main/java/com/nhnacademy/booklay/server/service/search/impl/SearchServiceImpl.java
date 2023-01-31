package com.nhnacademy.booklay.server.service.search.impl;

import com.nhnacademy.booklay.server.entity.Category;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.document.CategoryDocument;
import com.nhnacademy.booklay.server.entity.document.ProductDocument;
import com.nhnacademy.booklay.server.repository.category.CategoryRepository;
import com.nhnacademy.booklay.server.repository.documents.CategoryDocumentRepository;
import com.nhnacademy.booklay.server.repository.documents.ProductDocumentRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.service.search.SearchService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
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

    public SearchServiceImpl(CategoryDocumentRepository categoryDocumentRepository,
                             ProductDocumentRepository productDocumentRepository,
                             CategoryRepository categoryRepository, ProductRepository productRepository,
                             ElasticsearchOperations operations) {
        this.categoryDocumentRepository = categoryDocumentRepository;
        this.productDocumentRepository = productDocumentRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.operations = operations;
    }

    @Override
    public void saveAllDocuments(){
//        카테고리 인덱스 저장

        List<Category> categories = categoryRepository.findAll();
        List<CategoryDocument> categoryDocuments = new ArrayList<>();

        if(!categoryDocuments.isEmpty()){
            categories.forEach(category -> categoryDocuments.add(CategoryDocument.fromCategory(category)));
            categoryDocumentRepository.saveAll(categoryDocuments);
        }

//        상품 인덱스 지정

        List<Product> products = productRepository.findAll();
        List<ProductDocument> productDocuments = new ArrayList<>();


        if (!products.isEmpty()){
            products.forEach(product -> productDocuments.add(ProductDocument.fromEntity(product)));
            productDocumentRepository.saveAll(productDocuments);
        }

    }

    @Override
    public List<Long> retrieveProductsIdsByCategory(String categoryId) {

        return retrieveCategoryHitsByIdMatch(categoryId);
    }

    @Override
    public List<Long> retrieveProductsIdsByTags(String keywords) {
        return null;
    }

    @Override
    public List<Long> retrieveProductsIdsByKeywords(String keywords) {

        return retrieveProductHitsByKeywordsMatch(keywords);
    }


    /**
     * 카테고리 아이디를 받아서 엘라스틱 서치에 도큐먼트에 저장된 KeywordID 필드를 조회하여 상위 카테고리를 검색하는 경우
     * 하위 카테고리의 리스트도 모두 리스트로 나오게 됨.
     * @param categoryId 검색할 카테고리.
     * @return 검색된 카테고리들의 아이디 리스트.
     */
    @Override
    public List<Long> retrieveCategoryHitsByIdMatch(String categoryId) {
        Query nativeQuery = new NativeSearchQueryBuilder()
            .withQuery(
                QueryBuilders.matchQuery(KEYWORDS_ID, categoryId)
            )
            .build();

        SearchHits<CategoryDocument>
            categoryDocumentSearchHits = operations.search(nativeQuery, CategoryDocument.class);

        return getHitIds(categoryDocumentSearchHits);
    }

    /**
     * 여러 키워드가 들어간 스트링으로 검색하여 아이디 리스트를 반환.
     * @param keywords 검색할 키워드들이 들어간 스트링.
     * @return 검색된 상품들의 아이디 리스트.
     */

    @Override
    public List<Long> retrieveProductHitsByKeywordsMatch(String keywords) {
        Query nativeQuery = new NativeSearchQueryBuilder()
            .withQuery(
                QueryBuilders.matchQuery(KEYWORDS_TEXT, keywords).minimumShouldMatch("80%")
            )
            .build();

        SearchHits<ProductDocument>
            productDocumentSearchHits = operations.search(nativeQuery, ProductDocument.class);

        return getHitIds(productDocumentSearchHits);
    }

    /**
     * 검색된 결과들의 아이디 리스트만 리턴해주는 메소드,
     * 조회한 정보를 MySQL을 통해 페이지를 리턴하기 때문에 ID 리스트만 리턴.
     * @param searchHits 검색된 도큐먼트.
     * @return 아이디 리스트.
     * @param <T> 도큐먼트 타입.
     */
    private static <T> List<Long> getHitIds(SearchHits<T> searchHits) {
        List<Long> hitIds = new ArrayList<>();

        searchHits.getSearchHits().forEach(
            productDocumentSearchHit -> hitIds.add(
                Long.valueOf(Objects.requireNonNull(productDocumentSearchHit.getId())))
        );
        return hitIds;
    }
}
