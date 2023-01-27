package com.nhnacademy.booklay.server.controller.admin.product;

import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductBookRequest;
import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductSubscribeRequest;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductBookResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductSubscribeResponse;
import com.nhnacademy.booklay.server.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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


  //책 등록
  @PostMapping(value = "/books",
      consumes = {MediaType.APPLICATION_JSON_VALUE,
          MediaType.MULTIPART_FORM_DATA_VALUE})
  public Long postBookRegister(
      @RequestPart CreateUpdateProductBookRequest request,
      @RequestPart MultipartFile imgFile) throws Exception {
    request.setImage(imgFile);
    return productService.createBookProduct(request);
  }

  //책 수정용 조회
  @GetMapping("/books/{productId}")
  public RetrieveProductBookResponse getBookData(@PathVariable Long productId) {
    return productService.retrieveBookData(productId);
  }

  //책 수정
  @PutMapping(value = "/books",
      consumes = {MediaType.APPLICATION_JSON_VALUE,
          MediaType.MULTIPART_FORM_DATA_VALUE})
  public Long postBookUpdater(@RequestPart CreateUpdateProductBookRequest request,
      @RequestPart MultipartFile imgFile) throws Exception {
    request.setImage(imgFile);
    return productService.updateBookProduct(request);
  }

  //구독 등록
  @PostMapping(value = "/subscribes", consumes = {MediaType.APPLICATION_JSON_VALUE,
      MediaType.MULTIPART_FORM_DATA_VALUE})
  public Long postSubscribeRegister(@RequestPart CreateUpdateProductSubscribeRequest request,
      @RequestPart MultipartFile imgFile) throws Exception {
    request.setImage(imgFile);
    return productService.createSubscribeProduct(request);
  }

  //구독 수정용 조회
  @GetMapping("/subscribes/{productId}")
  public RetrieveProductSubscribeResponse getSubscribeData(@PathVariable Long productId) {
    return productService.retrieveSubscribeData(productId);
  }

  //구독 수정
  @PutMapping(value = "/subscribes",
      consumes = {MediaType.APPLICATION_JSON_VALUE,
          MediaType.MULTIPART_FORM_DATA_VALUE})
  public Long postSubscribeUpdater(@RequestPart CreateUpdateProductSubscribeRequest request,
      @RequestPart MultipartFile imgFile) throws Exception {
    request.setImage(imgFile);
    return productService.updateSubscribeProduct(request);
  }

  @DeleteMapping("/{productId}")
  public void softDeleteProduct(@PathVariable Long productId){
    productService.softDelete(productId);
  }
}
