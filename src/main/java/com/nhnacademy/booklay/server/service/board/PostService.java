package com.nhnacademy.booklay.server.service.board;

import com.nhnacademy.booklay.server.dto.board.request.BoardPostCreateRequest;

public interface PostService {

  Long createPost(BoardPostCreateRequest request);
}
