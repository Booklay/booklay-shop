package com.nhnacademy.booklay.server.dto.product.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateDeleteProductRelationRequest {

  @NotNull
  Long targetId;
  @NotNull
  Long baseId;

}
