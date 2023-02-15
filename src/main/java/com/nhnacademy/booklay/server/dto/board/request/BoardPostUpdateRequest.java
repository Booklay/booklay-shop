package com.nhnacademy.booklay.server.dto.board.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardPostUpdateRequest {

  @NotNull
  private Long postId;
  @NotNull
  private String title;
  @NotNull
  private String content;
  private Boolean viewPublic;
}
