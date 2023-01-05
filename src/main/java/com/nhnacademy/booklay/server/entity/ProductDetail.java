package com.nhnacademy.booklay.server.entity;

import lombok.*;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Table(name = "product_detail")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductDetail {

  @Id
  @Column(name="book_no")
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

  @Column(name="published_date")
  private LocalDate publishedDate;

  @Column(name="ebook_address")
  private String ebookAddress;

  @Column
  private Integer storage;
}
