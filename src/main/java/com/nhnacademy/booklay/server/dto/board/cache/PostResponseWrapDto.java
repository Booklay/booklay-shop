package com.nhnacademy.booklay.server.dto.board.cache;

import com.nhnacademy.booklay.server.dto.board.response.PostResponse;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class PostResponseWrapDto {
    private Page<PostResponse> data;
    private PostResponseWrapDto previous;
    private PostResponseWrapDto next;
    private Long productId;
}
