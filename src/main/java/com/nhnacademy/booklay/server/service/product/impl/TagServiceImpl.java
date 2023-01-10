package com.nhnacademy.booklay.server.service.product.impl;

import com.nhnacademy.booklay.server.dto.product.tag.request.CreateTagRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.UpdateTagRequest;
import com.nhnacademy.booklay.server.entity.Tag;
import com.nhnacademy.booklay.server.repository.product.TagRepository;
import com.nhnacademy.booklay.server.service.product.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

  private final TagRepository tagRepository;


  @Override
  public void createTag(CreateTagRequest request) {
    //같은 name 으로도 등록되면 안되는거 아닌가?, 예외처리 만들어주자

    Tag tag = Tag.builder()
        .name(request.getName())
        .build();
    tagRepository.save(tag);
  }

  @Override
  public void updateTag(UpdateTagRequest request) {
    //TODO: 예외처리 할것
    if (!tagRepository.existsById(request.getId())) {

    }
    Tag tag = Tag.builder()
        .name(request.getName())
        .build();
    tag.setId(request.getId());
    tagRepository.save(tag);
  }

  @Override
  public void deleteTag(Long id) {
//TODO: 예외처리 할것
    if (!tagRepository.existsById(id)) {

    }
    tagRepository.deleteById(id);
  }
}
