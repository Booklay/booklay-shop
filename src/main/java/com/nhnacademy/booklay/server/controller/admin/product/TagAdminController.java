package com.nhnacademy.booklay.server.controller.admin.product;

import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.product.tag.request.CreateDeleteTagProductRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.CreateTagRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.UpdateTagRequest;
import com.nhnacademy.booklay.server.dto.product.DeleteIdRequest;
import com.nhnacademy.booklay.server.dto.product.tag.response.RetrieveTagResponse;
import com.nhnacademy.booklay.server.dto.product.tag.response.TagProductResponse;
import com.nhnacademy.booklay.server.service.product.TagService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 최규태
 */

@Slf4j
@RestController
@RequestMapping("/admin/tag")
@RequiredArgsConstructor
public class TagAdminController {
  private final TagService tagService;

  //태그 자체만
  @GetMapping
  public PageResponse<RetrieveTagResponse> tagPage(Pageable pageable) {
    Page<RetrieveTagResponse> response = tagService.retrieveAllTag(pageable);
    return new PageResponse<>(response);
  }

  @PostMapping
  public void tagRegister(@Valid @RequestBody CreateTagRequest request) {
    tagService.createTag(request);
  }

  @PutMapping
  public void tagUpdate(@Valid @RequestBody UpdateTagRequest request) {
    tagService.updateTag(request);
  }

  @DeleteMapping
  public void tagDelete(@Valid @RequestBody DeleteIdRequest id) {
    tagService.deleteTag(id);
  }

  //태그-작품 연동
  @GetMapping("/product/{productNo}")
  public PageResponse<TagProductResponse> tagProductPage(Pageable pageable,
      @PathVariable Long productNo){

    log.info("상품 번호 : " + productNo);

    Page<TagProductResponse> response =  tagService.retrieveAllTagWithBoolean(pageable, productNo);

    List<TagProductResponse> tplist = response.getContent();
    for(TagProductResponse i : tplist){
      log.info("컨트롤러에서 시험 출력 : " + i.getId() + i.getName() + i.isRegistered());
    }
    return new PageResponse<>(response);
  }

  //태그 작품 연동 생성
  @PostMapping("/product")
  public void tagProductConnect(@Valid @RequestBody CreateDeleteTagProductRequest request){
    log.info("출력 : "+request.getProductNo());
    tagService.connectTagProduct(request);
  }

  @DeleteMapping("/product")
  public void tagProductDisconnect(@Valid @RequestBody CreateDeleteTagProductRequest request){
    log.info("출력 : "+request.getProductNo());
    tagService.disconnectTagProduct(request);
  }
}
