package com.nhnacademy.booklay.server.dto.product.tag.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateDeleteTagProductRequest {

  Long tagId;
  Long productNo;

  public CreateDeleteTagProductRequest(Long tagId, Long productNo) {
    this.tagId = tagId;
    this.productNo = productNo;
  }
}
