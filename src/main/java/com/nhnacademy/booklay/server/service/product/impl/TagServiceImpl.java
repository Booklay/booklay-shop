package com.nhnacademy.booklay.server.service.product.impl;

import com.nhnacademy.booklay.server.dto.product.tag.request.CreateDeleteTagProductRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.CreateTagRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.UpdateTagRequest;
import com.nhnacademy.booklay.server.dto.product.DeleteIdRequest;
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

  private final TagRepository tagRepository;
  private final ProductTagRepository productTagRepository;
  private final ProductRepository productRepository;


  @Override
  @Transactional
  public void createTag(CreateTagRequest request) {
    //같은 name 으로도 등록되면 안되는거 아닌가?, 예외처리 만들어주자
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
  public void deleteTag(DeleteIdRequest id) {
    Long requestId = id.getId();
    tagExistValidator(requestId);
    productTagRepository.deleteByPk_TagId(requestId);
    tagRepository.deleteById(requestId);
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
      throw new NotFoundException(Product.class, "Product not found");
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

    for (TagProductResponse i : convertedContent) {
      log.info("시험 출력 : " + i.getId() + i.getName() + i.isRegistered());
    }

    return new PageImpl<TagProductResponse>(convertedContent, basicPageDto.getPageable(),
        basicPageDto.getTotalElements());
  }

  @Override
  public void connectTagProduct(CreateDeleteTagProductRequest request) {
    log.info("제품 번호 : " + request.getProductNo());
    log.info("태그 번호 : " + request.getTagId());

    ProductTag.Pk pk = new Pk(request.getProductNo(), request.getTagId());
    if (!productRepository.existsById(request.getProductNo())) {
      throw new NotFoundException(Product.class, "product not found");
    }
    Product product = productRepository.findById(request.getProductNo()).orElseThrow();
    if (!tagRepository.existsById(request.getTagId())) {
      throw new NotFoundException(Tag.class, "product not found");
    }
    Tag tag = tagRepository.findById(request.getTagId()).orElseThrow();

    ProductTag productTag = ProductTag.builder()
        .pk(pk)
        .product(product)
        .tag(tag)
        .build();

    productTagRepository.save(productTag);
  }

  @Override
  public void disconnectTagProduct(CreateDeleteTagProductRequest request) {
    ProductTag.Pk pk = new Pk(request.getProductNo(), request.getTagId());
    if (!productRepository.existsById(request.getProductNo())) {
      throw new NotFoundException(Product.class, "product not found");
    }
    if (!tagRepository.existsById(request.getTagId())) {
      throw new NotFoundException(Tag.class, "product not found");
    }

    productTagRepository.deleteById(pk);
  }
}
