package com.nhnacademy.booklay.server.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/product")
public class ProductRegisterController {

  @GetMapping
  private String productMain(){

    return "web/admin/product/main";
  }
}
