package com.nhnacademy.booklay.server.dto.product.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class CreateUpdateProductBookRequest {

  private Long productId;
  @Setter
  private MultipartFile image;
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
  @DateTimeFormat(pattern = "yyyy-MM-dd hh:MM:ss")
  @Setter
  private LocalDateTime createdAt;

  private Long productDetailId;
  @NotNull
  private String isbn;
  @NotNull
  private int page;
  @NotNull
  private String publisher;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @NotNull
  private LocalDate publishedDate;
  @Setter
  private String ebookAddress;
  @Setter
  private int storage;
  @NotNull
  private List<Long> authorIds;
  @NotNull
  private List<Long> categoryIds;

  @Builder
  public CreateUpdateProductBookRequest(Long productId, String title, Long price,
      Long pointRate, String shortDescription, String longDescription, boolean isSelling,
      boolean pointMethod, Long productDetailId, String isbn, int page, String publisher,
      LocalDate publishedDate, String ebookAddress, int storage, List<Long> authorIds,
      List<Long> categoryIds) {
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
    this.authorIds = authorIds;
    this.categoryIds = categoryIds;
  }
}
