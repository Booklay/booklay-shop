package com.nhnacademy.booklay.server.dto.product.request;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class CreateUpdateProductSubscribeRequest {

  private Long productId;
  @NotNull
  private String title;
  @Setter
  private MultipartFile image;
  @NotNull
  private Long price;
  @NotNull
  private Long pointRate;
  @NotNull
  private String shortDescription;
  @NotNull
  private String longDescription;
  @NotNull
  private Boolean isSelling;
  @NotNull
  private Boolean pointMethod;
  @DateTimeFormat(pattern = "yyyy-MM-dd hh:MM:ss")
  @Setter
  private LocalDateTime createdAt;

  @NotNull
  private List<Long> categoryIds;

  private Long subscribeId;
  @NotNull
  @Length(max = 4)
  private Long subscribeWeek;
  @NotNull
  @Length(max = 7)
  private Long subscribeDay;
  @NotNull
  private String publisher;
  private List<Long> childProducts;

  public CreateUpdateProductSubscribeRequest(Long productId, String title, Long price,
      Long pointRate, String shortDescription, String longDescription, Boolean isSelling,
      Boolean pointMethod, List<Long> categoryIds, Long subscribeId, Long subscribeWeek,
      Long subscribeDay, String publisher, List<Long> childProducts) {
    this.productId = productId;
    this.title = title;
    this.price = price;
    this.pointRate = pointRate;
    this.shortDescription = shortDescription;
    this.longDescription = longDescription;
    this.isSelling = isSelling;
    this.pointMethod = pointMethod;
    this.categoryIds = categoryIds;
    this.subscribeId = subscribeId;
    this.subscribeWeek = subscribeWeek;
    this.subscribeDay = subscribeDay;
    this.publisher = publisher;
    this.childProducts = childProducts;
  }
}
