package com.nhnacademy.booklay.server.dto.product;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteIdRequest {

  @NotNull
  Long id;

}
