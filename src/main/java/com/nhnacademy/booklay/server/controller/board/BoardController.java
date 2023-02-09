package com.nhnacademy.booklay.server.controller.board;

import com.nhnacademy.booklay.server.dto.board.request.BoardPostCreateRequest;
import com.nhnacademy.booklay.server.service.board.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

  private final PostService postService;

  @PostMapping
  public Long createPost(@RequestBody BoardPostCreateRequest request) {
    return postService.createPost(request);
  }

}
