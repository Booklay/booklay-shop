package com.nhnacademy.booklay.server.dto.product.response;

import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.dto.product.tag.response.RetrieveTagResponse;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductDetail;
import com.nhnacademy.booklay.server.entity.Subscribe;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder.In;
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
  private Boolean isSelling;
  @NotNull
  private Boolean pointMethod;

  private List<RetrieveTagResponse> productTags;

  //책 상세
  private Long productDetailId;
  private String isbn;
  private Integer page;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate publishedDate;
  @Setter
  private String ebookAddress;
  @Setter
  private Integer storage;
  private List<RetrieveAuthorResponse> authors;

  //구독 상품 상세
  private Long subscribeId;
  @Length(max = 4)
  private Integer subscribeWeek;
  @Length(max = 7)
  private Integer subscribeDay;
  private List<Long> childProducts;

  //책 구독 상품 상세 공통
  @NotNull
  private String publisher;

  public RetrieveProductViewResponse(Product product, ProductDetail productDetail,
      List<RetrieveAuthorResponse> authors, List<RetrieveTagResponse> productTags) {
    this.productId = product.getId();
    this.image = null;
    this.title = product.getTitle();
    this.price = product.getPrice();
    this.pointRate = product.getPointRate();
    this.shortDescription = product.getShortDescription();
    this.longDescription = product.getLongDescription();
    this.isSelling = product.isSelling();
    this.pointMethod = product.isPointMethod();
    this.productTags = productTags;

    this.productDetailId = productDetail.getId();
    this.isbn = productDetail.getIsbn();
    this.page = productDetail.getPage();
    this.publishedDate = productDetail.getPublishedDate();
    this.ebookAddress = productDetail.getEbookAddress();
    this.storage = productDetail.getStorage();
    this.authors = authors;
    this.publisher = productDetail.getPublisher();
  }

  public RetrieveProductViewResponse(Product product, Subscribe subscribe,
      List<RetrieveTagResponse> productTags) {
    this.productId = product.getId();
    this.image = null;
    this.title = product.getTitle();
    this.price = product.getPrice();
    this.pointRate = product.getPointRate();
    this.shortDescription = product.getShortDescription();
    this.longDescription = product.getLongDescription();
    this.isSelling = product.isSelling();
    this.pointMethod = product.isPointMethod();
    this.productTags = productTags;

    this.subscribeId = subscribe.getId();
    this.subscribeWeek = subscribe.getSubscribeWeek();
    this.subscribeDay = subscribe.getSubscribeDay();
//    this.childProducts = childProducts;
    this.publisher = subscribe.getPublisher();
  }
}
