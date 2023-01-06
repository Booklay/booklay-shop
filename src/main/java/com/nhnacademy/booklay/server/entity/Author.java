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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long authorNo;

  @Setter
  @OneToOne
  @JoinColumn(name = "member_no")
  private Member member;

  @Column(length = 50)
  private String name;

  @Builder
  public Author(Long authorNo, String name) {
    this.authorNo = authorNo;
    this.name = name;
  }

}
