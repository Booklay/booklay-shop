package com.nhnacademy.booklay.server.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="product")
public class Product {
  @Id
  @Column(name="product_no", nullable = false)
  private long productNo;

  @Column
  private long thumbnailNo;

  @Column
  private String title;

  @Column
  private LocalDateTime registedAt;

  @Column
  private int price;

  @Column
  private int pointRate;

  @Column
  private String shortDescription;

  @Column
  private String longDescription;

  @Column
  private boolean isSelling;

  @Column
  private boolean pointMethod;
}
