package com.nhnacademy.booklay.server.dto.product.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@AllArgsConstructor
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
  private Boolean isSelling;
  @NotNull
  private Boolean pointMethod;
  @DateTimeFormat(pattern = "yyyy-MM-dd hh:MM:ss")
  @NotNull
  private LocalDateTime createdAt;

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
}
