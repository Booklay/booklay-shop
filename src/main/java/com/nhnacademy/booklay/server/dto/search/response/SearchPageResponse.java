package com.nhnacademy.booklay.server.dto.search.response;

import java.util.List;
import lombok.Getter;

@Getter
public class SearchPageResponse<T> {

  private final String searchKeywords;
  
  private final Integer pageNumber;

  private final Integer pageSize;

  private final Integer totalPages; // 총 페이지 수

  private final List<T> data;

  public SearchPageResponse(String searchKeywords, Integer pageNumber, Integer pageSize,
                            Integer totalPages, List<T> data) {
    this.searchKeywords = searchKeywords;
    this.pageNumber = pageNumber;
    this.pageSize = pageSize;
    this.totalPages = totalPages;
    this.data = data;
  }
}
