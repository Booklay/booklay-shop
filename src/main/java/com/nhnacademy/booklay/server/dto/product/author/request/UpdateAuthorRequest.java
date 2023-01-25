package com.nhnacademy.booklay.server.dto.product.author.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateAuthorRequest {

  @NotNull
  Long id;

  @NotNull
  String name;

  Long memberNo;

  public UpdateAuthorRequest(Long id, String name, Long memberNo) {
    this.id = id;
    this.name = name;
    this.memberNo = memberNo;
  }
}
