package com.nhnacademy.booklay.server.controller.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.service.product.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 최규태
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class ProductController {

  private final ProductService productService;

  @GetMapping
  public PageResponse<RetrieveProductResponse> retrieveProductPage(Pageable pageable)
      throws JsonProcessingException {
    Page<RetrieveProductResponse> response = productService.retrieveProductPage(pageable);

    ObjectMapper mapper = new ObjectMapper();
    List<RetrieveProductResponse> content = response.getContent();
    for(int i=0; i<content.size(); i++){
      RetrieveProductResponse response1 = content.get(i);

      log.info("출투더력 : "+mapper.writeValueAsString(response1));
    }
    return new PageResponse<>(response);
  }
}
