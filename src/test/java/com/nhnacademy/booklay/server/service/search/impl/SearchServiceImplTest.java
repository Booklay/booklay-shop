package com.nhnacademy.booklay.server.service.search.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.nhnacademy.booklay.server.dto.product.response.ProductAllInOneResponse;
import com.nhnacademy.booklay.server.dto.search.request.SearchIdRequest;
import com.nhnacademy.booklay.server.dto.search.request.SearchKeywordsRequest;
import com.nhnacademy.booklay.server.dto.search.response.SearchPageResponse;
import com.nhnacademy.booklay.server.dto.search.response.SearchProductResponse;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.document.ProductDocument;
import com.nhnacademy.booklay.server.repository.category.CategoryRepository;
import com.nhnacademy.booklay.server.repository.documents.CategoryDocumentRepository;
import com.nhnacademy.booklay.server.repository.documents.ProductDocumentRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchHitsImpl;
import org.springframework.data.elasticsearch.core.TotalHitsRelation;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.test.util.ReflectionTestUtils;

@Slf4j
@ExtendWith(MockitoExtension.class)
class SearchServiceImplTest {

    @Mock
    private CategoryDocumentRepository categoryDocumentRepository;

    @Mock
    private ProductDocumentRepository productDocumentRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ElasticsearchOperations operations;

    @InjectMocks
    private SearchServiceImpl searchService;

    private List<ProductAllInOneResponse> productList;

    private SearchKeywordsRequest request;

    @BeforeEach
    void setUp() {

        productList = List.of(
            new ProductAllInOneResponse(
                DummyCart.getDummyProduct(DummyCart.getDummyProductBookDto()),
                DummyCart.getDummyProductDetail(DummyCart.getDummyProductBookDto()),
                null)
        );

        request = new SearchKeywordsRequest();


    }

    @Test
    void getAllProducts() {

        SearchHits<ProductDocument>
            productDocumentSearchHits = new SearchHitsImpl<>(1L, TotalHitsRelation.OFF,1L, null, List.of(), null,null);

        given(operations.search(any(Query.class), eq(ProductDocument.class)))
            .willReturn(productDocumentSearchHits);

        SearchPageResponse<SearchProductResponse> pageResponse = searchService.getAllProducts(Pageable.ofSize(20));

        assertThat(pageResponse.getData()).isNotNull();
    }

    @Test
    void getLatestProducts() {

        // given
        SearchHits<ProductDocument>
            productDocumentSearchHits = new SearchHitsImpl<>(1L, TotalHitsRelation.OFF,1L, null, List.of(), null,null);

        given(operations.search(any(Query.class), eq(ProductDocument.class)))
            .willReturn(productDocumentSearchHits);

        // when
        searchService.getLatestProducts();

        BDDMockito.then(operations).should().search((Query) any(), any());
    }

    @Test
    void searchProductsByKeywords() {

        SearchHits<ProductDocument>
            productDocumentSearchHits = new SearchHitsImpl<>(1L, TotalHitsRelation.OFF,1L, null, List.of(), null,null);

        given(operations.search(any(Query.class), eq(ProductDocument.class)))
            .willReturn(productDocumentSearchHits);


        ReflectionTestUtils.setField(request,"classification","keywords");
        ReflectionTestUtils.setField(request,"keywords","키워드");

        SearchPageResponse<SearchProductResponse>
            pageResponse = searchService.searchProductsByKeywords(request,Pageable.ofSize(20));

        assertThat(pageResponse.getData()).isNotNull();
    }

    @Test
    void searchProductsByKeywords_ByKeywordTextField() {

        SearchHits<ProductDocument>
            productDocumentSearchHits = new SearchHitsImpl<>(1L, TotalHitsRelation.OFF,1L, null, List.of(), null,null);

        given(operations.search(any(Query.class), eq(ProductDocument.class)))
            .willReturn(productDocumentSearchHits);


        ReflectionTestUtils.setField(request,"classification","categories");
        ReflectionTestUtils.setField(request,"keywords","국내");

        SearchPageResponse<SearchProductResponse>
            pageResponse = searchService.searchProductsByKeywords(request,Pageable.ofSize(20));

        assertThat(pageResponse.getData()).isNotNull();
    }

    @Test
    void searchProductsByCategory() {

        SearchHits<ProductDocument>
            productDocumentSearchHits = new SearchHitsImpl<>(1L, TotalHitsRelation.OFF,1L, null, List.of(), null,null);

        given(operations.search(any(Query.class), eq(ProductDocument.class)))
            .willReturn(productDocumentSearchHits);


        SearchIdRequest request1 = new SearchIdRequest();

        ReflectionTestUtils.setField(request1,"classification","categories");
        ReflectionTestUtils.setField(request1,"id",1L);

        searchService.searchProductsByCategory(request1,Pageable.ofSize(20));

        assertDoesNotThrow(
            () -> searchService.searchProductsByCategory(request1,Pageable.ofSize(20)));
    }

    @Test
    void saveAllDocuments() {

        given(productRepository.retrieveAllProducts()).willReturn(productList);

        searchService.saveAllDocuments();

        then(productDocumentRepository).should(times(1)).saveAll(any());
    }
}