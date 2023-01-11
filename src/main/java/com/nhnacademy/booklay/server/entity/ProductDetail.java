package com.nhnacademy.booklay.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@Table(name = "product_detail")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ProductDetail {

  @Id
  @Column(name="book_no")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter
  private Long id;

  @OneToOne
  @JoinColumn(name="product_no")
  private Product product;

  @Column(length = 20)
  private String isbn;

  @Column
  private Integer page;

  @Column
  private String publisher;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @Column(name="published_date")
  private LocalDate publishedDate;

  @Setter
  @Column(name="ebook_address")
  private String ebookAddress;

  @Setter
  @Column
  private Integer storage;

  @Builder
  public ProductDetail(Product product, String isbn, Integer page, String publisher, LocalDate publishedDate) {
    this.product = product;
    this.isbn = isbn;
    this.page = page;
    this.publisher = publisher;
    this.publishedDate = publishedDate;
  }
}
