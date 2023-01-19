package com.nhnacademy.booklay.server.dto.product.response;

import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class RetrieveProductViewResponse {

  //상품
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

  //책 상세
  private Long productDetailId;
  private String isbn;
  private int page;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate publishedDate;
  @Setter
  private String ebookAddress;
  @Setter
  private int storage;
  private List<Long> authorIds;
  private List<Long> categoryIds;

  //구독 상품 상세
  private Long subscribeId;
  @Length(max = 4)
  private Long subscribeWeek;
  @Length(max = 7)
  private Long subscribeDay;
  private List<Long> childProducts;

  //책 구독 상품 상세 공통
  @NotNull
  private String publisher;
}
