package com.nhnacademy.booklay.server.dto.product.author.response;

import com.nhnacademy.booklay.server.entity.Member;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RetrieveAuthorResponse {

  @NotNull
  Long authorNo;

  Member member;

  @NotNull
  String name;


  public RetrieveAuthorResponse(Long authorNo, Member member, String name) {
    this.authorNo = authorNo;
    this.name = name;
  }
}
