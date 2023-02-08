package com.nhnacademy.booklay.server.controller.admin.product;

import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.product.request.CreateDeleteProductRelationRequest;
import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductBookRequest;
import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductSubscribeRequest;
import com.nhnacademy.booklay.server.dto.product.request.DisAndConnectBookWithSubscribeRequest;
import com.nhnacademy.booklay.server.dto.product.response.ProductAllInOneResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveBookForSubscribeResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductSubscribeResponse;
import com.nhnacademy.booklay.server.service.product.BookSubscribeService;
import com.nhnacademy.booklay.server.service.product.ProductRelationService;
import com.nhnacademy.booklay.server.service.product.ProductService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 최규태
 */

@Slf4j
@RestController
@RequestMapping("/admin/product")
@RequiredArgsConstructor
public class ProductAdminController {

    private final ProductService productService;
    private final BookSubscribeService bookSubscribeService;
    private final ProductRelationService productRelationService;

    /**
     * 관리자의 전체 상품 목록 조회
     * @param pageable
     * @return
     * @throws IOException
     */
    @GetMapping
    public PageResponse<RetrieveProductResponse> postAdminProduct(Pageable pageable)
        throws IOException {
        Page<RetrieveProductResponse> response = productService.retrieveAdminProductPage(pageable);

        return new PageResponse<>(response);
    }

    /**
     * 서적 상품 등록
     * @param request
     * @param imgFile
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/books",
                 consumes = { MediaType.APPLICATION_JSON_VALUE,
                     MediaType.MULTIPART_FORM_DATA_VALUE })
    public Long postBookRegister(
        @RequestPart CreateUpdateProductBookRequest request,
        @RequestPart MultipartFile imgFile) throws Exception {
        request.setImage(imgFile);
        return productService.createBookProduct(request);
    }

    /**
     * 책 수정 조회
     * @param productId
     * @return
     */
    // 책 수정용 조회
    @GetMapping("/books/{productId}")
    public ProductAllInOneResponse getBookData(@PathVariable Long productId) {
        return productService.retrieveBookData(productId);
    }

    /**
     * 책 수정 요청
     * @param request
     * @param imgFile
     * @return
     * @throws Exception
     */
    @PutMapping(value = "/books",
                consumes = { MediaType.APPLICATION_JSON_VALUE,
                    MediaType.MULTIPART_FORM_DATA_VALUE })
    public Long postBookUpdater(@RequestPart CreateUpdateProductBookRequest request,
                                @RequestPart MultipartFile imgFile) throws Exception {
        request.setImage(imgFile);
        return productService.updateBookProduct(request);
    }

    /**
     * 구독 상품 등록
     * @param request
     * @param imgFile
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/subscribes", consumes = { MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE })
    public Long postSubscribeRegister(@RequestPart CreateUpdateProductSubscribeRequest request,
                                      @RequestPart MultipartFile imgFile) throws Exception {
        request.setImage(imgFile);
        return productService.createSubscribeProduct(request);
    }

    /**
     * 구독 상품 수정 조회
     * @param productId
     * @return
     */
    @GetMapping("/subscribes/{productId}")
    public ProductAllInOneResponse getSubscribeData(@PathVariable Long productId) {
        return productService.retrieveBookData(productId);
    }

    /**
     * 구독 상품 수정 요청
     * @param request
     * @param imgFile
     * @return
     * @throws Exception
     */
    @PutMapping(value = "/subscribes",
                consumes = { MediaType.APPLICATION_JSON_VALUE,
                    MediaType.MULTIPART_FORM_DATA_VALUE })
    public Long postSubscribeUpdater(@RequestPart CreateUpdateProductSubscribeRequest request,
                                     @RequestPart MultipartFile imgFile) throws Exception {
        request.setImage(imgFile);
        return productService.updateSubscribeProduct(request);
    }

    /**
     * 구독 상품 하위 상품 등록 조회
     * @param pageable
     * @param subscribeId
     * @return
     */
    @GetMapping("/subscribes/connect/{subscribeId}")
    public PageResponse<RetrieveBookForSubscribeResponse> getBooksDataForSubscribe(
        Pageable pageable,
        @PathVariable Long subscribeId) {
        Page<RetrieveBookForSubscribeResponse> response =
            productService.retrieveBookDataForSubscribe(pageable, subscribeId);
        return new PageResponse<>(response);
    }

    /**
     * 상품과 구독 상품 등록 요청
     * @param request
     */
    @PostMapping("/subscribes/connect/{subscribeId}")
    public void booksAndSubscribeConnect(
        @RequestBody DisAndConnectBookWithSubscribeRequest request) {
        bookSubscribeService.bookSubscribeConnection(request);
    }

    /**
     * 상품과 구독 상품 등록 취소
     * @param request
     */
    @DeleteMapping("/subscribes/connect/{subscribeId}")
    public void booksAndSubscribeDisconnect(
        @RequestBody DisAndConnectBookWithSubscribeRequest request) {
        bookSubscribeService.bookSubscribeDisconnection(request);
    }

    /**
     * 연관 상품 등록창 조회
     * @param productNo
     * @param pageable
     * @return
     * @throws IOException
     */
    @GetMapping("/recommend/{productNo}")
    public PageResponse<RetrieveProductResponse> retrieveRecommendConnector(
        @PathVariable Long productNo, Pageable pageable) throws IOException {
        Page<RetrieveProductResponse> response = productRelationService.retrieveRecommendConnection(productNo, pageable);

        return new PageResponse<>(response);
    }

    /**
     * 연관 상품 등록
     * @param request
     */
    @PostMapping("/recommend")
    public void createRecommend(@RequestBody CreateDeleteProductRelationRequest request){
        productRelationService.createProductRelation(request);
    }

    /**
     * 연관 상품 등록 취소
     * @param request
     */
    @DeleteMapping("/recommend")
    public void deleteRecommend(@RequestBody CreateDeleteProductRelationRequest request){
        productRelationService.deleteProductRelation(request);
    }


    /**
     * 소프트 딜리트
     * @param productId
     */
    @DeleteMapping("/{productId}")
    public void softDeleteProduct(@PathVariable Long productId) {
        productService.softDelete(productId);
    }
}
