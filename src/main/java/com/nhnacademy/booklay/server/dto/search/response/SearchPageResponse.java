package com.nhnacademy.booklay.server.dto.search.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SearchPageResponse<T> {

  private final String searchKeywords;
  private final Long totalHits;
  private final Integer pageNumber;
  private final String averageScore;
  private final Integer pageSize;
  private final Integer totalPages; // 총 페이지 수
  private final List<T> data;

  @Builder
  public SearchPageResponse(String searchKeywords, Long totalHits, Integer pageNumber,
                            String averageScore, Integer pageSize, Integer totalPages, List<T> data) {
    this.searchKeywords = searchKeywords;
    this.totalHits = totalHits;
    this.pageNumber = pageNumber;
    this.averageScore = averageScore;
    this.pageSize = pageSize;
    this.totalPages = totalPages;
    this.data = data;
  }
}
