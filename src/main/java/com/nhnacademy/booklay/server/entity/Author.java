package com.nhnacademy.booklay.server.entity;

import javax.persistence.*;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Author {

  @Id
  @Column(name="author_no")
  private Long authorNo;

  @OneToOne
  @JoinColumn(name = "member_no")
  private Member member;

  @Column(length = 50)
  private String name;

  @Builder
  public Author(Long authorNo, Member member, String name) {
    this.authorNo = authorNo;
    this.member = member;
    this.name = name;
  }

}
