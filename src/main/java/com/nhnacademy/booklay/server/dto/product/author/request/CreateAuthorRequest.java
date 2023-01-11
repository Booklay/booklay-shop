package com.nhnacademy.booklay.server.dto.product.author.request;

import com.nhnacademy.booklay.server.entity.Member;
import lombok.Getter;

@Getter
public class CreateAuthorRequest {
  Member member;
  String name;
}
