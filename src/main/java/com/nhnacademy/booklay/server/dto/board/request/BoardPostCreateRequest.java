package com.nhnacademy.booklay.server.dto.board.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardPostCreateRequest {

  Integer postTypeNo;
  Long memberNo;
  Long productNo;
  Long groupPostNo;
  Integer groupOrder;
  Integer depth;
  String title;
  String content;
  Boolean viewPublic;
  Boolean answered;
}