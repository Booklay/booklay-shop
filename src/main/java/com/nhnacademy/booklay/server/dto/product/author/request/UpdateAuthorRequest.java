package com.nhnacademy.booklay.server.dto.product.author.request;

import com.nhnacademy.booklay.server.entity.Member;

public class UpdateAuthorRequest {
  Long authorNo;
  Member member;
  String name;
}
