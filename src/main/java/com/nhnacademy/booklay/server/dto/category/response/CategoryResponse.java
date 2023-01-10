package com.nhnacademy.booklay.server.dto.category.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryResponse {
  private Long id;
  private String name;
  private Long parentCategoryId;
  private Boolean isExposure;
}