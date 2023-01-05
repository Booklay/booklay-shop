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
  private long bookNo;

  @OneToOne
  @JoinColumn(name="product_no")
  private Product productNo;

  @Column(length = 20)
  private String isbn;

  @Column
  private int page;

  @Column(length = 100)
  private String publisher;

  @Column
  private LocalDate publishedDate;

  @Column
  private String ebookAddress;

  @Column
  private int storage;
}
