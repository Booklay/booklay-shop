package com.nhnacademy.booklay.server.entity;

import javax.persistence.*;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Table
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
