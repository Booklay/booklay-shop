package com.nhnacademy.booklay.server.dto.category.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductBoardCategoryResponse {

  private Long id;
  private String name;
  private Long parentCategoryId;
}
