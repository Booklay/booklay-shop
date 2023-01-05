package com.nhnacademy.booklay.server.entity;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Table
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Author {

  @Id
  @Column(name="author_no")
  private Long authorNo;

  @OneToOne
  @JoinColumn(name = "member_no")
  private Member member;

  @Column(length = 50)
  private String name;
}
