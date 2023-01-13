package com.nhnacademy.booklay.server.dto.product.author.request;

import lombok.Getter;

@Getter
public class UpdateAuthorRequest {
  Long authorNo;
  String name;
  Long memberNo;

  public UpdateAuthorRequest(Long authorNo, String name, Long memberNo) {
    this.authorNo = authorNo;
    this.name = name;
    this.memberNo = memberNo;
  }
}
