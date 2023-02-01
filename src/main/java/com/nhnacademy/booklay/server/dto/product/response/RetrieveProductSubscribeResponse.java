package com.nhnacademy.booklay.server.dto.product.response;

import com.nhnacademy.booklay.server.dto.category.response.CategoryResponse;
import com.nhnacademy.booklay.server.dto.product.tag.response.RetrieveTagResponse;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.Subscribe;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor
public class RetrieveProductSubscribeResponse {

    @NotNull
    private Long productId;
    @NotNull
    private String title;
    private Long objectFileId;
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
    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    @Setter
    private List<CategoryResponse> categoryList;

    @Setter
    private List<RetrieveTagResponse> tagList;

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

    public RetrieveProductSubscribeResponse(Product product, Subscribe subscribe) {
        this.productId = product.getId();
        this.title = product.getTitle();
        this.objectFileId = product.getObjectFile().getId();
        this.price = product.getPrice();
        this.pointRate = product.getPointRate();
        this.shortDescription = product.getShortDescription();
        this.longDescription = product.getLongDescription();
        this.selling = product.isSelling();
        this.pointMethod = product.isPointMethod();
        this.createdAt = product.getCreatedAt();

        this.subscribeId = subscribe.getId();
        this.subscribeWeek = subscribe.getSubscribeWeek();
        this.subscribeDay = subscribe.getSubscribeDay();
        this.publisher = subscribe.getPublisher();
    }
}
