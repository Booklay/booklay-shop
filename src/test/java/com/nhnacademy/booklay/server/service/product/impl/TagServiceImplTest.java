package com.nhnacademy.booklay.server.service.product.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.nhnacademy.booklay.server.dto.product.tag.request.CreateTagRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.UpdateTagRequest;
import com.nhnacademy.booklay.server.entity.Tag;
import com.nhnacademy.booklay.server.repository.product.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest
class TagServiceImplTest {

  //TODO: Mock Test로 바꿀것...

  @Autowired
  TagServiceImpl tagService;

  @Autowired
  TagRepository tagRepository;

  CreateTagRequest request;
  Tag tag;

  @BeforeEach
  void setup(){
    request = new CreateTagRequest("요즘_도서_태그는_이래!");
  }

  @Test
  void testTagCreate_success(){

    assertDoesNotThrow(()->tagService.createTag(request));
  }

  @Test
  void testTagCreate_failure(){

  }

  @Test
  void testTagUpdate_success(){
    Tag seed= Tag.builder()
        .name(request.getName())
        .build();

    Tag original = tagRepository.save(seed);

    log.info("출력 : " + original.getId());

    UpdateTagRequest update = new UpdateTagRequest(original.getId(), "옛날태그");

    tagService.updateTag(update);

    Tag updated = tagRepository.findById(original.getId()).orElseThrow(()-> new IllegalArgumentException("not found"));

    assertThat(updated.getId()).isEqualTo(original.getId());
    assertThat(updated.getName()).isNotEqualTo(original.getName());

  }

  @Test
  void testTagUpdate_failure(){

  }

  @Test
  void testTagDelete_success(){
    Tag seed= Tag.builder()
        .name(request.getName())
        .build();

    Tag original = tagRepository.save(seed);

    tagService.deleteTag(original.getId());

    assertThat(tagRepository.findById(original.getId())).isEmpty();
  }

  @Test
  void testTagDelete_failure(){

  }
}