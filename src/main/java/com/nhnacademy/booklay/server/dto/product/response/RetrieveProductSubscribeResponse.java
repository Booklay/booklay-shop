package com.nhnacademy.booklay.server.dto.product.response;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@AllArgsConstructor
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
  private Boolean isSelling;
  @NotNull
  private Boolean pointMethod;
  @DateTimeFormat(pattern = "yyyy-MM-dd hh:MM:ss")
  @NotNull
  private LocalDateTime createdAt;

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
}
