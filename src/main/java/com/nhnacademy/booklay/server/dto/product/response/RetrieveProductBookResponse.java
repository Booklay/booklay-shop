package com.nhnacademy.booklay.server.dto.product.response;

import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
public class RetrieveProductBookResponse {
  @NotNull
  private Long productId;
  @NotNull
  private String title;
  @NotNull
  private Long price;
  @NotNull
  private Long pointRate;
  @NotNull
  private String shortDescription;
  @NotNull
  private String longDescription;
  @NotNull
  private boolean isSelling;
  @NotNull
  private boolean pointMethod;

  @NotNull
  private Long productDetailId;
  @NotNull
  private String isbn;
  @NotNull
  private Integer page;
  @NotNull
  private String publisher;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @NotNull
  private LocalDate publishedDate;
  @Setter
  private String ebookAddress;
  @Setter
  private Integer storage;

  @Setter
  private List<Long> authorIds;
  @Setter
  private List<Long> categoryIds;

  public RetrieveProductBookResponse(Long productId, String title, Long price, Long pointRate,
      String shortDescription, String longDescription, boolean isSelling, boolean pointMethod,
      Long productDetailId, String isbn, Integer page, String publisher, LocalDate publishedDate,
      String ebookAddress, Integer storage) {
    this.productId = productId;
    this.title = title;
    this.price = price;
    this.pointRate = pointRate;
    this.shortDescription = shortDescription;
    this.longDescription = longDescription;
    this.isSelling = isSelling;
    this.pointMethod = pointMethod;
    this.productDetailId = productDetailId;
    this.isbn = isbn;
    this.page = page;
    this.publisher = publisher;
    this.publishedDate = publishedDate;
    this.ebookAddress = ebookAddress;
    this.storage = storage;
  }
}
