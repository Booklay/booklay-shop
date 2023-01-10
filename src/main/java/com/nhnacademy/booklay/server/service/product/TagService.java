package com.nhnacademy.booklay.server.service.product;

import com.nhnacademy.booklay.server.dto.product.tag.request.CreateTagRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.UpdateTagRequest;

public interface TagService {
  void createTag(CreateTagRequest request);
  void updateTag(UpdateTagRequest request);
  void deleteTag(Long id);
}
