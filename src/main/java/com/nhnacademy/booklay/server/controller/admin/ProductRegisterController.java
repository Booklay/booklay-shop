package com.nhnacademy.booklay.server.controller.admin;

import com.nhnacademy.booklay.server.service.product.ProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/product")
@RequiredArgsConstructor
public class ProductRegisterController {

  private final ProductDetailService productDetailService;

  //관리 페이지로
  @GetMapping
  private String productMain(){

    return "web/admin/product/main";
  }

  //등록

  //책
  @GetMapping("/register/book")
  private String getBookRegister(){

    return null;
  }


  @PostMapping("/register/book")
  private String postBookRegister(){

    return null;
  }

  //구독
  @GetMapping("/register/subscribe")
  private String getSubscribeRegister(){

    return null;
  }


  @PostMapping("/register/subscribe")
  private String postSubscribeRegister(){

    return null;
  }
}
