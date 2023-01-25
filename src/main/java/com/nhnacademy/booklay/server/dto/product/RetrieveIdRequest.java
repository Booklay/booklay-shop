package com.nhnacademy.booklay.server.dto.product;

import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RetrieveIdRequest {

  @NotNull
  Long id;

  public RetrieveIdRequest(Long id) {
    this.id = id;
  }
}
