package com.nhnacademy.booklay.server.service.product;

import com.nhnacademy.booklay.server.dto.product.tag.request.CreateTagRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.UpdateTagRequest;
import com.nhnacademy.booklay.server.dto.product.tag.response.RetrieveTagResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TagService {
  void createTag(CreateTagRequest request);
  void updateTag(UpdateTagRequest request);
  Page<RetrieveTagResponse> retrieveAllTag(Pageable pageable);
  void deleteTag(Long id);
}
