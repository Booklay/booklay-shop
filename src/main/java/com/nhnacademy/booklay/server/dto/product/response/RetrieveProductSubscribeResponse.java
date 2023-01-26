package com.nhnacademy.booklay.server.dto.product.response;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class RetrieveProductSubscribeResponse {

  @NotNull
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
  private boolean isSelling;
  @NotNull
  private boolean pointMethod;
  @DateTimeFormat(pattern = "yyyy-MM-dd hh:MM:ss")
  @NotNull
  private LocalDateTime registedAt;

  @NotNull
  @Setter
  private List<Long> categoryIds;

  @NotNull
  private Long subscribeId;
  @NotNull
  @Length(max = 4)
  private Integer subscribeWeek;
  @NotNull
  @Length(max = 7)
  private Integer subscribeDay;
  @NotNull
  private String publisher;
  @Setter
  private List<Long> childProducts;

  public RetrieveProductSubscribeResponse(Long productId, String title, Long price, Long pointRate,
      String shortDescription, String longDescription, boolean isSelling, boolean pointMethod,
      LocalDateTime registedAt, Long subscribeId, Integer subscribeWeek, Integer subscribeDay,
      String publisher) {
    this.productId = productId;
    this.title = title;
    this.price = price;
    this.pointRate = pointRate;
    this.shortDescription = shortDescription;
    this.longDescription = longDescription;
    this.isSelling = isSelling;
    this.pointMethod = pointMethod;
    this.registedAt = registedAt;
    this.subscribeId = subscribeId;
    this.subscribeWeek = subscribeWeek;
    this.subscribeDay = subscribeDay;
    this.publisher = publisher;
  }
}
