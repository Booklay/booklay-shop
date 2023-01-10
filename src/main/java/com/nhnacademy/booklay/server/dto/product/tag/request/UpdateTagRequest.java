package com.nhnacademy.booklay.server.dto.product.tag.request;

import lombok.Getter;

@Getter
public class UpdateTagRequest {
  private Long id;
  private String name;

  public UpdateTagRequest(Long id, String name) {
    this.id = id;
    this.name = name;
  }
}
