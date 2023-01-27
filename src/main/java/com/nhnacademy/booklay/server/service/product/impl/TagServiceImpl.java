package com.nhnacademy.booklay.server.service.product.impl;

import com.nhnacademy.booklay.server.dto.product.DeleteIdRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.CreateDeleteTagProductRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.CreateTagRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.UpdateTagRequest;
import com.nhnacademy.booklay.server.dto.product.tag.response.RetrieveTagResponse;
import com.nhnacademy.booklay.server.dto.product.tag.response.TagProductResponse;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductTag;
import com.nhnacademy.booklay.server.entity.ProductTag.Pk;
import com.nhnacademy.booklay.server.entity.Tag;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.repository.product.ProductTagRepository;
import com.nhnacademy.booklay.server.repository.product.TagRepository;
import com.nhnacademy.booklay.server.service.product.TagService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 최규태
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

  private static final String NOT_FOUNT = "product not found";
  private final TagRepository tagRepository;
  private final ProductTagRepository productTagRepository;
  private final ProductRepository productRepository;


  @Override
  @Transactional
  public void createTag(CreateTagRequest request) {
    tagNameValidator(request.getName());

    Tag tag = Tag.builder()
        .name(request.getName())
        .build();
    tagRepository.save(tag);
  }

  @Override
  @Transactional
  public void updateTag(UpdateTagRequest request) {
    tagExistValidator(request.getId());
    tagNameValidator(request.getName());
    Tag tag = Tag.builder()
        .name(request.getName())
        .build();
    tag.setId(request.getId());
    tagRepository.save(tag);
  }

  @Override
  @Transactional
  public Page<RetrieveTagResponse> retrieveAllTag(Pageable pageable) {
    return tagRepository.findAllBy(pageable, RetrieveTagResponse.class);
  }

  @Override
  @Transactional
  public void deleteTag(DeleteIdRequest request) {
    Long id = request.getId();
    tagExistValidator(id);

    if (productTagRepository.existsByPk_TagId(id)) {
      productTagRepository.deleteByPk_TagId(id);
    }
    tagRepository.deleteById(id);
  }

  private void tagExistValidator(Long id) {
    if (!tagRepository.existsById(id)) {
      throw new NotFoundException(Tag.class, "tag not found");
    }
  }

  private void tagNameValidator(String name) {
    if (tagRepository.existsByName(name)) {
      throw new IllegalArgumentException(name + " tag is already exist");
    }
  }

  @Override
  public Page<TagProductResponse> retrieveAllTagWithBoolean(Pageable pageable, Long productNo) {
    if (!productRepository.existsById(productNo)) {
      throw new NotFoundException(Product.class, NOT_FOUNT);
    }
    Page<RetrieveTagResponse> basicPageDto = tagRepository.findAllBy(pageable,
        RetrieveTagResponse.class);

    List<RetrieveTagResponse> basicContent = basicPageDto.getContent();
    List<TagProductResponse> convertedContent = new ArrayList<>();

    for (RetrieveTagResponse response : basicContent) {
      ProductTag.Pk ptPk = new Pk(productNo, response.getId());

      Boolean isRegistered = productTagRepository.existsById(ptPk);

      TagProductResponse tagProductDto = new TagProductResponse(response.getId(),
          response.getName(), isRegistered);

      convertedContent.add(tagProductDto);
    }

    return new PageImpl<>(convertedContent, basicPageDto.getPageable(),
        basicPageDto.getTotalElements());
  }

  @Override
  public void createTagProduct(CreateDeleteTagProductRequest request) {
    ProductTag.Pk pk = new Pk(request.getProductNo(), request.getTagId());

    Product product = productRepository.findById(request.getProductNo()).orElseThrow(()->new NotFoundException(Product.class, "product not exists"));
    Tag tag = tagRepository.findById(request.getTagId()).orElseThrow(()->new NotFoundException(Tag.class, "tag not exists"));

    ProductTag productTag = ProductTag.builder()
        .pk(pk)
        .product(product)
        .tag(tag)
        .build();

    productTagRepository.save(productTag);
  }

  @Override
  public void deleteTagProduct(CreateDeleteTagProductRequest request) {
    ProductTag.Pk pk = new Pk(request.getProductNo(), request.getTagId());
    productRepository.findById(request.getProductNo()).orElseThrow(()->new NotFoundException(Product.class, "product not found"));
    tagRepository.findById(request.getTagId()).orElseThrow(()->new NotFoundException(Tag.class, "tag not found"));

    productTagRepository.deleteById(pk);
  }
}
