package com.nhnacademy.booklay.server.dto.product.response;

import com.nhnacademy.booklay.server.dto.category.response.CategoryResponse;
import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.dto.product.tag.response.RetrieveTagResponse;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductDetail;
import com.nhnacademy.booklay.server.entity.Subscribe;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor
public class RetrieveProductBookResponse {

  private Long productId;
  private String title;
  private Long objectFileId;
  private Long price;
  private Long pointRate;
  private String shortDescription;
  private String longDescription;
  private Boolean selling;
  private Boolean pointMethod;
  @DateTimeFormat(pattern = "yyyy-MM-dd hh:MM:ss")
  private LocalDateTime createdAt;

  private Long productDetailId;
  private String isbn;
  private Integer page;
  private String publisher;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate publishedDate;
  private String ebookAddress;
  private Integer storage;

  @Setter
  private List<CategoryResponse> categoryList;
  @Setter
  private List<RetrieveTagResponse> tagList;
  @Setter
  private List<RetrieveAuthorResponse> authorList;

  public RetrieveProductBookResponse(Product product, ProductDetail productDetail) {
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
    this.productDetailId = productDetail.getId();
    this.isbn = productDetail.getIsbn();
    this.page = productDetail.getPage();
    this.publisher = productDetail.getPublisher();
    this.publishedDate = productDetail.getPublishedDate();
    this.ebookAddress = productDetail.getEbookAddress();
    this.storage = productDetail.getStorage();

  }
}
