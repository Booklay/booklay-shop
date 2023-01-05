package com.nhnacademy.booklay.server.entity;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="product_detail")
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
  private int page;

  @Column(length = 100)
  private String publisher;

  @Column(name="published_date")
  private LocalDate publishedDate;

  @Column(name="ebook_address")
  private String ebookAddress;

  @Column
  private int storage;
}
