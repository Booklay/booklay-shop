package com.nhnacademy.booklay.server.controller.admin.product;

import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.product.request.CreateDeleteProductRelationRequest;
import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductBookRequest;
import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductSubscribeRequest;
import com.nhnacademy.booklay.server.dto.product.request.DisAndConnectBookWithSubscribeRequest;
import com.nhnacademy.booklay.server.dto.product.response.ProductAllInOneResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveBookForSubscribeResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.service.product.BookSubscribeService;
import com.nhnacademy.booklay.server.service.product.ProductRelationService;
import com.nhnacademy.booklay.server.service.product.ProductService;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
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
   *
   * @param pageable
   * @return
   * @throws IOException
   */
  @GetMapping
  public ResponseEntity<PageResponse<RetrieveProductResponse>> postAdminProduct(Pageable pageable)
      throws IOException {
    Page<RetrieveProductResponse> response = productService.retrieveAdminProductPage(pageable);
    PageResponse<RetrieveProductResponse> body = new PageResponse<>(response);

    return ResponseEntity.status(HttpStatus.OK).body(body);
  }

  /**
   * 서적 상품 등록
   *
   * @param request
   * @param imgFile
   * @return
   * @throws Exception
   */
  @PostMapping(value = "/books",
      consumes = {MediaType.APPLICATION_JSON_VALUE,
          MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<Long> postBookRegister(
      @RequestPart CreateUpdateProductBookRequest request,
      @RequestPart MultipartFile imgFile) throws Exception {
    request.setImage(imgFile);
    Long body = productService.createBookProduct(request);

    return ResponseEntity.status(HttpStatus.CREATED).body(body);
  }

  /**
   * 책 수정 조회
   *
   * @param productId
   * @return
   */
  // 책 수정용 조회
  @GetMapping("/books/{productId}")
  public ResponseEntity<ProductAllInOneResponse> getBookData(@PathVariable Long productId) {
    ProductAllInOneResponse body = productService.retrieveBookData(productId);

    return ResponseEntity.status(HttpStatus.OK).body(body);
  }

  /**
   * 책 수정 요청
   *
   * @param request
   * @param imgFile
   * @return
   * @throws Exception
   */
  @PutMapping(value = "/books",
      consumes = {MediaType.APPLICATION_JSON_VALUE,
          MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<Long> postBookUpdater(@RequestPart CreateUpdateProductBookRequest request,
      @Nullable @RequestPart MultipartFile imgFile) throws Exception {
    if (Objects.nonNull(imgFile)) {
      request.setImage(imgFile);
    }
    Long body = productService.updateBookProduct(request);

    return ResponseEntity.status(HttpStatus.OK).body(body);
  }

  /**
   * 구독 상품 등록
   *
   * @param request
   * @param imgFile
   * @return
   * @throws Exception
   */
  @PostMapping(value = "/subscribes", consumes = {MediaType.APPLICATION_JSON_VALUE,
      MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<Long> postSubscribeRegister(@RequestPart CreateUpdateProductSubscribeRequest request,
      @RequestPart MultipartFile imgFile) throws Exception {
    request.setImage(imgFile);
    Long body =  productService.createSubscribeProduct(request);

    return ResponseEntity.status(HttpStatus.OK).body(body);
  }

  /**
   * 구독 상품 수정 조회
   *
   * @param productId
   * @return
   */
  @GetMapping("/subscribes/{productId}")
  public ResponseEntity<ProductAllInOneResponse> getSubscribeData(@PathVariable Long productId) {
    ProductAllInOneResponse body =  productService.retrieveBookData(productId);

    return ResponseEntity.status(HttpStatus.OK).body(body);
  }

  /**
   * 구독 상품 수정 요청
   *
   * @param request
   * @param imgFile
   * @return
   * @throws Exception
   */
  @PutMapping(value = "/subscribes",
      consumes = {MediaType.APPLICATION_JSON_VALUE,
          MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<Long> postSubscribeUpdater(@RequestPart CreateUpdateProductSubscribeRequest request,
      @Nullable @RequestPart MultipartFile imgFile) throws Exception {
    if (Objects.nonNull(imgFile)) {
      request.setImage(imgFile);
    }
    Long body = productService.updateSubscribeProduct(request);

    return ResponseEntity.status(HttpStatus.OK).body(body);
  }

  /**
   * 구독 상품 하위 상품 등록 조회
   *
   * @param pageable
   * @param subscribeId
   * @return
   */
  @GetMapping("/subscribes/connect/{subscribeId}")
  public ResponseEntity<PageResponse<RetrieveBookForSubscribeResponse>> getBooksDataForSubscribe(
      Pageable pageable,
      @PathVariable Long subscribeId) {
    Page<RetrieveBookForSubscribeResponse> response = productService.retrieveBookDataForSubscribe(pageable, subscribeId);
    PageResponse<RetrieveBookForSubscribeResponse> body = new PageResponse<>(response);

    return ResponseEntity.status(HttpStatus.OK).body(body);
  }

  /**
   * 상품과 구독 상품 등록 요청
   *
   * @param request
   */
  @PostMapping("/subscribes/connect/{subscribeId}")
  public ResponseEntity<Void> booksAndSubscribeConnect(
      @RequestBody DisAndConnectBookWithSubscribeRequest request) {
    bookSubscribeService.bookSubscribeConnection(request);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  /**
   * 상품과 구독 상품 등록 취소
   *
   * @param request
   */
  @DeleteMapping("/subscribes/connect/{subscribeId}")
  public ResponseEntity<Void> booksAndSubscribeDisconnect(
      @RequestBody DisAndConnectBookWithSubscribeRequest request) {
    bookSubscribeService.bookSubscribeDisconnection(request);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  /**
   * 연관 상품 등록창 조회
   *
   * @param productNo
   * @param pageable
   * @return
   * @throws IOException
   */
  @GetMapping("/recommend/{productNo}")
  public ResponseEntity<PageResponse<RetrieveProductResponse>> retrieveRecommendConnector(
      @PathVariable Long productNo, Pageable pageable) throws IOException {
    Page<RetrieveProductResponse> response = productRelationService.retrieveRecommendConnection(
        productNo, pageable);

    PageResponse<RetrieveProductResponse> body = new PageResponse<>(response);

    return ResponseEntity.status(HttpStatus.OK).body(body);
  }

  /**
   * 연관 상품 등록
   *
   * @param request
   */
  @PostMapping("/recommend")
  public ResponseEntity<Void> createRecommend(@RequestBody CreateDeleteProductRelationRequest request) {
    productRelationService.createProductRelation(request);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  /**
   * 연관 상품 등록 취소
   *
   * @param request
   */
  @DeleteMapping("/recommend")
  public ResponseEntity<Void> deleteRecommend(@RequestBody CreateDeleteProductRelationRequest request) {
    productRelationService.deleteProductRelation(request);

    return ResponseEntity.status(HttpStatus.OK).build();
  }


  /**
   * 소프트 딜리트
   *
   * @param productId
   */
  @DeleteMapping("/{productId}")
  public ResponseEntity<Void> softDeleteProduct(@PathVariable Long productId) {
    productService.softDelete(productId);

    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
