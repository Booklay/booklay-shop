package com.nhnacademy.booklay.server.controller.product;

import com.nhnacademy.booklay.server.dto.product.request.CreateWishlistRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

  //private final

  @PostMapping
  public String createWishlist(CreateWishlistRequest request){

    return null;
  }
}
