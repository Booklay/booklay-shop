package com.nhnacademy.booklay.server.dto.board.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardPostCreateRequest {

  private Integer postTypeNo;
  private Long memberNo;
  private Long productNo;
  private Long groupNo;
  private Integer groupOrderNo;
  private Integer depth;
  private String title;
  private String content;
  private Boolean viewPublic;
  private Boolean answered;
}