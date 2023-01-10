package com.nhnacademy.booklay.server.controller.admin;

import com.nhnacademy.booklay.server.dto.product.request.CreateProductBookRequest;
import com.nhnacademy.booklay.server.dto.product.request.CreateProductSubscribeRequest;
import com.nhnacademy.booklay.server.service.category.CategoryService;
import com.nhnacademy.booklay.server.service.product.AuthorService;
import com.nhnacademy.booklay.server.service.product.CategoryProductService;
import com.nhnacademy.booklay.server.service.product.ProductAuthorService;
import com.nhnacademy.booklay.server.service.product.ProductDetailService;
import com.nhnacademy.booklay.server.service.product.ProductService;
import com.nhnacademy.booklay.server.service.product.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author 최규태
 */


@RestController
@RequestMapping("/admin/product")
@RequiredArgsConstructor
public class ProductRegisterController {

  private final ProductDetailService productDetailService;
  private final ProductService productService;
  private final AuthorService authorService;
  private final ProductAuthorService productAuthorService;
  private final CategoryService categoryService;
  private final CategoryProductService categoryProductService;
  private final SubscribeService subscribeService;

  //책 등록
  @PostMapping("/register/book")
  public Long postBookRegister(CreateProductBookRequest request) throws Exception {
    //product
    Long savedProductId = productService.createBookProduct(request);
    return savedProductId;
  }

  //책 수정
  @PutMapping("/register/book")
  public Long postBookUpdater(CreateProductBookRequest request) throws Exception {

    Long savedId = productService.updateBookProduct(request);
    //id 넘겨줘서 이걸로 프론트단에서 상세페이지 이동하게? 해야할듯
    return savedId;
  }


  //구독
  @PostMapping("/register/subscribe")
  public Long postSubscribeRegister(CreateProductSubscribeRequest request) throws Exception {
    Long savedId = productService.createSubscribeProduct(request);

    return savedId;
  }

  //구독 수정
  @PostMapping("/update/subscribe")
  public Long postSubscribeUpdate(CreateProductSubscribeRequest request) throws Exception {
    Long savedId = productService.updateSubscribeProduct(request);
    return savedId;
  }

}
