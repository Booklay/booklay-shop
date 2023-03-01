package com.nhnacademy.booklay.server.dto.product.request;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUpdateProductSubscribeRequest {

    private Long productId;
    @NotNull
    private String title;
    @Setter
    private MultipartFile image;
    private Long originalImage;
    @NotNull
    private Long price;
    @NotNull
    private Long pointRate;
    @NotNull
    private String shortDescription;
    @NotNull
    private String longDescription;
    @NotNull
    private Boolean selling;
    @NotNull
    private Boolean pointMethod;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:MM:ss")
    @Setter
    private LocalDateTime createdAt;

    @NotNull
    private List<Long> categoryIds;

    private Long subscribeId;

    @Length(max = 4)
    private Integer subscribeWeek;
    @Length(max = 7)
    private Integer subscribeDay;
    private List<Long> childProducts;

    @NotNull
    private String publisher;
}
