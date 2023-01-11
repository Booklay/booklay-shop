package com.nhnacademy.booklay.server.dto.product.author.response;

import com.nhnacademy.booklay.server.dto.product.product.response.AuthorBookResponse;
import com.nhnacademy.booklay.server.entity.Member;
import java.util.List;

public class RetrieveAuthorResponse {

  Long authorNo;
  Member member;
  String name;
  List<AuthorBookResponse> bookList;
}
