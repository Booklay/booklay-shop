package com.nhnacademy.booklay.server.dto.product.response;

import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductDetail;
import com.nhnacademy.booklay.server.entity.Subscribe;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class RetrieveProductResponse {

  @NotNull
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
  private boolean isSelling;
  @NotNull
  private boolean pointMethod;
  @NotNull
  private String shortDescription;
  @NotNull
  private String publisher;

  private List<RetrieveAuthorResponse> authors;

  public RetrieveProductResponse(Product product, ProductDetail productDetail,
      List<RetrieveAuthorResponse> authorsNo) {
    this.productId = product.getId();
    this.title = product.getTitle();
    this.price = product.getPrice();
    this.pointRate = product.getPointRate();
    this.isSelling = product.isSelling();
    this.pointMethod = product.isPointMethod();
    this.shortDescription = product.getShortDescription();
    this.publisher = productDetail.getPublisher();
    this.authors = authorsNo;
  }

  public RetrieveProductResponse(Product product, Subscribe subscribe) {
    this.productId = product.getId();
    this.title = product.getTitle();
    this.price = product.getPrice();
    this.pointRate = product.getPointRate();
    this.isSelling = product.isSelling();
    this.pointMethod = product.isPointMethod();
    this.shortDescription = product.getShortDescription();
    this.publisher = subscribe.getPublisher();
  }
}
