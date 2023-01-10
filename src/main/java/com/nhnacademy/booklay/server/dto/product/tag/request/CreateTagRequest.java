package com.nhnacademy.booklay.server.dto.product.tag.request;

import lombok.Getter;

@Getter
public class CreateTagRequest {
  private String name;

  public CreateTagRequest(String name) {
    this.name = name;
  }
}
