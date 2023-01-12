package com.nhnacademy.booklay.server.controller.admin;

import com.nhnacademy.booklay.server.dto.product.request.CreateProductBookRequest;
import com.nhnacademy.booklay.server.dto.product.request.CreateProductSubscribeRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.CreateTagRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.UpdateTagRequest;
import com.nhnacademy.booklay.server.dto.product.tag.response.RetrieveTagResponse;
import com.nhnacademy.booklay.server.service.product.ProductService;
import com.nhnacademy.booklay.server.service.product.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

  private final ProductService productService;
  private final TagService tagService;

  //책 등록
  @PostMapping("/register/book")
  public Long postBookRegister(CreateProductBookRequest request) throws Exception {
    //product
    return productService.createBookProduct(request);
  }

  //책 수정
  @PutMapping("/register/book")
  public Long postBookUpdater(CreateProductBookRequest request) throws Exception {

    return productService.updateBookProduct(request);
  }


  //구독
  @PostMapping("/register/subscribe")
  public Long postSubscribeRegister(CreateProductSubscribeRequest request) throws Exception {
    return  productService.createSubscribeProduct(request);
  }

  //구독 수정
  @PutMapping("/update/subscribe")
  public Long postSubscribeUpdate(CreateProductSubscribeRequest request) throws Exception {
    return productService.updateSubscribeProduct(request);
  }


  //태그 자체만
  @GetMapping("/tag")
  public Page<RetrieveTagResponse> tagPage(Pageable pageable){
    return tagService.retrieveAllTag(pageable);
  }
  @PostMapping("/tag")
  public void tagRegister(CreateTagRequest request) throws Exception {
    tagService.createTag(request);
  }

  @PutMapping("/tag")
  public void tagUpdate(UpdateTagRequest request) throws Exception {
    tagService.updateTag(request);
  }

  @DeleteMapping("/tag")
  public void tagDelete(Long id) {
    tagService.deleteTag(id);
  }

}
