package com.nhnacademy.booklay.server.service.product;

import com.nhnacademy.booklay.server.dto.product.tag.request.CreateTagRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.CreateDeleteTagProductRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.UpdateTagRequest;
import com.nhnacademy.booklay.server.dto.product.DeleteIdRequest;
import com.nhnacademy.booklay.server.dto.product.tag.response.RetrieveTagResponse;
import com.nhnacademy.booklay.server.dto.product.tag.response.TagProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author 최규태
 */

public interface TagService {
  void createTag(CreateTagRequest request);
  void updateTag(UpdateTagRequest request);
  Page<RetrieveTagResponse> retrieveAllTag(Pageable pageable);
  Page<TagProductResponse> retrieveAllTagWithBoolean(Pageable pageable, Long productNo);
  void deleteTag(DeleteIdRequest id);
  void connectTagProduct(CreateDeleteTagProductRequest request);
  void disconnectTagProduct(CreateDeleteTagProductRequest request);
}
