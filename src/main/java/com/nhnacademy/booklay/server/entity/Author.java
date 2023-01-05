package com.nhnacademy.booklay.server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Author {

  @Id
  @Column(name="author_no")
  private Long authorNo;

  //TODO : 맴버 들어와야 합니다.

  @Column(length = 50)
  private String name;
}
