package com.nhnacademy.booklay.server.dto.product;

import com.nhnacademy.booklay.server.entity.Image;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ProductSubscribeDto {

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
  @NotNull
  private List<Long> categoryIds;

  @Setter
  private Long subscribeId;
  @NotNull
  private int subscribeWeek;
  @NotNull
  private int subscribeDay;
  @Setter
  private String publisher;


  public ProductSubscribeDto(Image image, String title, Long price, Long pointRate,
      String shortDescription, String longDescription, boolean isSelling, boolean pointMethod,
      List<Long> categoryIds, int subscribeWeek, int subscribeDay) {
    this.image = image;
    this.title = title;
    this.price = price;
    this.pointRate = pointRate;
    this.shortDescription = shortDescription;
    this.longDescription = longDescription;
    this.isSelling = isSelling;
    this.pointMethod = pointMethod;
    this.categoryIds = categoryIds;
    this.subscribeWeek = subscribeWeek;
    this.subscribeDay = subscribeDay;
  }
}
