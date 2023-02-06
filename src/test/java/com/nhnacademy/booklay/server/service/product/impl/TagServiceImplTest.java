package com.nhnacademy.booklay.server.service.product.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.nhnacademy.booklay.server.dto.product.DeleteIdRequest;
import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductBookRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.CreateDeleteTagProductRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.CreateTagRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.UpdateTagRequest;
import com.nhnacademy.booklay.server.dto.product.tag.response.RetrieveTagResponse;
import com.nhnacademy.booklay.server.dto.product.tag.response.TagProductResponse;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductTag;
import com.nhnacademy.booklay.server.entity.ProductTag.Pk;
import com.nhnacademy.booklay.server.entity.Tag;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.repository.product.ProductTagRepository;
import com.nhnacademy.booklay.server.repository.product.TagRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author 최규태
 */

@Slf4j
@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

  @InjectMocks
  TagServiceImpl tagService;

  @Mock
  TagRepository tagRepository;

  @Mock
  ProductTagRepository productTagRepository;

  @Mock
  ProductRepository productRepository;

  CreateTagRequest request;
  Tag tag;

  @BeforeEach
  void setup() {

    request = new CreateTagRequest("요즘_도서_태그는_이래!");

    tag = Tag.builder()
        .name(request.getName())
        .build();
  }

  @Test
  void testTagCreate_success() {
    assertDoesNotThrow(() -> tagService.createTag(request));
  }

  @Test
  void testTagCreateFailure_tagNameDuplicated() throws Exception {
    given(tagRepository.existsByName(request.getName())).willReturn(true);

    assertThatThrownBy(() -> tagService.createTag(request)).isInstanceOf(
        IllegalArgumentException.class);
  }

  @Test
  void testTagUpdate_success() throws Exception {
    Long ID = 1L;

    ReflectionTestUtils.setField(tag, "id", ID);

    UpdateTagRequest update = new UpdateTagRequest(tag.getId(), "옛날태그");

    Tag newTag = Tag.builder()
        .name(update.getName())
        .build();
    ReflectionTestUtils.setField(newTag, "id", ID);

    given(tagRepository.existsById(tag.getId())).willReturn(true);
    given(tagRepository.findById(tag.getId())).willReturn(Optional.of(newTag));

    tagService.updateTag(update);

    Tag expected = tagRepository.findById(tag.getId())
        .orElseThrow(() -> new IllegalArgumentException("not found"));

    assertThat(expected.getId()).isEqualTo(tag.getId());
    assertThat(expected.getName()).isNotEqualTo(tag.getName());
  }

  @Test
  void testTagUpdate_failureByNameDuplicated() {
    UpdateTagRequest update = new UpdateTagRequest(1L, "겹치는_태그");

    given(tagRepository.existsById(update.getId())).willReturn(true);
    given(tagRepository.existsByName(update.getName())).willReturn(true);

    assertThatThrownBy(() -> tagService.updateTag(update)).isInstanceOf(
        IllegalArgumentException.class);
  }

  @Test
  void testTagUpdate_failureByIdNotExist() {
    UpdateTagRequest update = new UpdateTagRequest(1L, "겹치는_태그");

    given(tagRepository.existsById(update.getId())).willReturn(false);
    assertThatThrownBy(() -> tagService.updateTag(update)).isInstanceOf(NotFoundException.class);
  }

  @Test
  void testTagDelete_success() {
    ReflectionTestUtils.setField(tag, "id", 1L);

    given(tagRepository.existsById(tag.getId())).willReturn(true);
    DeleteIdRequest deleteIdRequest = new DeleteIdRequest(tag.getId());
    tagService.deleteTag(deleteIdRequest);

    assertThat(tagRepository.findById(tag.getId())).isEmpty();
  }

  @Test
  void testTagDelete_failure() {
    ReflectionTestUtils.setField(tag, "id", 1L);
    given(tagRepository.existsById(tag.getId())).willReturn(false);

    DeleteIdRequest deleteIdRequest = new DeleteIdRequest(tag.getId());

    assertThatThrownBy(() -> tagService.deleteTag(deleteIdRequest)).isInstanceOf(
        NotFoundException.class);
  }

  @Test
  void testRetrieveTagPage_success(){
    //given
    given(tagRepository.findAllBy(any(), any())).willReturn(Page.empty());

    //when
    Page<RetrieveTagResponse> pageResponse = tagService.retrieveAllTag(PageRequest.of(0, 10));

    //then
    BDDMockito.then(tagRepository).should().findAllBy(any(), any());

    assertThat(pageResponse.getTotalElements()).isZero();
  }

  @Test
  void testRetrieveTagPageWithBoolean_success(){
//given
    given(productRepository.existsById(any())).willReturn(true);

    given(tagRepository.findAllBy(any(), any())).willReturn(Page.empty());

    //when
    Page<TagProductResponse> pageResponse = tagService.retrieveAllTagWithBoolean(PageRequest.of(0, 10), any());

    //then
    BDDMockito.then(tagRepository).should().findAllBy(any(), any());

    assertThat(pageResponse.getTotalElements()).isZero();
  }

  @Test
  void testCreateTagProduct_success(){
    //given
    CreateUpdateProductBookRequest request1 = DummyCart.getDummyProductBookDto();
    Product product = DummyCart.getDummyProduct(request1);
    tag.setId(1L);
    CreateDeleteTagProductRequest createTagProductRequest = new CreateDeleteTagProductRequest(tag.getId(), product.getId());

    given(tagRepository.findById(tag.getId())).willReturn(Optional.of(tag));
    given(productRepository.findById(product.getId())).willReturn(Optional.of(product));

    //when
    tagService.createTagProduct(createTagProductRequest);

    //then
    BDDMockito.then(productTagRepository).should().save(any());
  }

  @Test
  void testCreateTagProduct_failureByTagNotFound(){
    //given
    CreateUpdateProductBookRequest request1 = DummyCart.getDummyProductBookDto();
    Product product = DummyCart.getDummyProduct(request1);
    tag.setId(1L);
    CreateDeleteTagProductRequest createTagProductRequest = new CreateDeleteTagProductRequest(tag.getId(), product.getId());

    given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
    given(tagRepository.findById(tag.getId())).willThrow(new NotFoundException(Tag.class, "tag not found"));

    //then
    assertThatThrownBy(()->tagService.createTagProduct(createTagProductRequest)).isInstanceOf(NotFoundException.class);
  }

  @Test
  void testCreateTagProduct_failureByProductNotFound(){
    CreateUpdateProductBookRequest request1 = DummyCart.getDummyProductBookDto();
    Product product = DummyCart.getDummyProduct(request1);
    tag.setId(1L);
    CreateDeleteTagProductRequest createTagProductRequest = new CreateDeleteTagProductRequest(tag.getId(), product.getId());

    given(productRepository.findById(product.getId())).willThrow(new NotFoundException(Product.class, "product not found"));

    assertThatThrownBy(()->tagService.createTagProduct(createTagProductRequest)).isInstanceOf(NotFoundException.class);
  }

  @Test
  void testDeleteTagProduct_success(){
    //given
    CreateUpdateProductBookRequest request1 = DummyCart.getDummyProductBookDto();
    Product product = DummyCart.getDummyProduct(request1);
    tag.setId(1L);
    CreateDeleteTagProductRequest deleteTagProductRequest = new CreateDeleteTagProductRequest(tag.getId(), product.getId());

    //when
    tagService.deleteTagProduct(deleteTagProductRequest);

    //then
    BDDMockito.then(productTagRepository).should().deleteById(any());
  }

  @Test
  void testDeleteTagProduct_failureByTag(){

    //given
    CreateUpdateProductBookRequest request1 = DummyCart.getDummyProductBookDto();
    Product product = DummyCart.getDummyProduct(request1);
    tag.setId(1L);
    CreateDeleteTagProductRequest deleteTagProductRequest = new CreateDeleteTagProductRequest(tag.getId(), product.getId());

    //when
    tagService.deleteTagProduct(deleteTagProductRequest);

    //then
    then(productTagRepository).should().deleteById(any());
  }

  @Test
  void testDeleteTagProduct_failureByProduct(){
    CreateUpdateProductBookRequest request1 = DummyCart.getDummyProductBookDto();
    Product product = DummyCart.getDummyProduct(request1);
    tag.setId(1L);
    CreateDeleteTagProductRequest deleteTagProductRequest = new CreateDeleteTagProductRequest(tag.getId(), product.getId());
    ProductTag.Pk pk = new Pk(tag.getId(), product.getId());

    //when
    tagService.deleteTagProduct(deleteTagProductRequest);

    //then
    then(productTagRepository).should().deleteById(any());

  }
}