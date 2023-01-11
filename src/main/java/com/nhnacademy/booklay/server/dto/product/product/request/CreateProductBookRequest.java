package com.nhnacademy.booklay.server.dto.product.product.request;

import com.nhnacademy.booklay.server.entity.Image;
import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class CreateProductBookRequest {

  @Setter
  private Long productId;
  @NotNull
  private Image image;
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

  @Setter
  private Long productDetailId;
  @NotNull
  private String isbn;
  @NotNull
  private int page;
  @NotNull
  private String publisher;
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
  public CreateProductBookRequest(Image image, String title, Long price, Long pointRate,
      String shortDescription, String longDescription, boolean isSelling, boolean pointMethod,
      String isbn, int page, String publisher, LocalDate publishedDate, List<Long> authorIds, List<Long> categoryIds) {
    this.image = image;
    this.title = title;
    this.price = price;
    this.pointRate = pointRate;
    this.shortDescription = shortDescription;
    this.longDescription = longDescription;
    this.isSelling = isSelling;
    this.pointMethod = pointMethod;
    this.isbn = isbn;
    this.page = page;
    this.publisher = publisher;
    this.publishedDate = publishedDate;
    this.authorIds = authorIds;
    this.categoryIds = categoryIds;
  }
}
