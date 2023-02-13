package com.nhnacademy.booklay.server.controller.admin.product;

import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.product.DeleteIdRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.CreateDeleteTagProductRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.CreateTagRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.UpdateTagRequest;
import com.nhnacademy.booklay.server.dto.product.tag.response.RetrieveTagResponse;
import com.nhnacademy.booklay.server.dto.product.tag.response.TagProductResponse;
import com.nhnacademy.booklay.server.service.product.TagService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // 태그 자체만
    @GetMapping
    public ResponseEntity<PageResponse> tagPage(Pageable pageable) {
        Page<RetrieveTagResponse> response = tagService.retrieveAllTag(pageable);
        PageResponse result = new PageResponse<>(response);
        return ResponseEntity.status(HttpStatus.OK)
            .body(result);
    }

    @PostMapping
    public ResponseEntity tagRegister(@Valid @RequestBody CreateTagRequest request) {
        tagService.createTag(request);
       return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity tagUpdate(@Valid @RequestBody UpdateTagRequest request) {
        tagService.updateTag(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping
    public ResponseEntity tagDelete(@Valid @RequestBody DeleteIdRequest id) {
        tagService.deleteTag(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 태그-작품 연동
    @GetMapping("/product/{productNo}")
    public ResponseEntity<PageResponse<TagProductResponse>> tagProductPage(Pageable pageable,
        @PathVariable Long productNo) {
        Page<TagProductResponse> response = tagService.retrieveAllTagWithBoolean(pageable, productNo);
        PageResponse result = new PageResponse<>(response);
        return ResponseEntity.status(HttpStatus.OK)
            .body(result);
    }

    // 태그 작품 연동 생성
    @PostMapping("/product")
    public ResponseEntity tagProductConnect(@Valid @RequestBody CreateDeleteTagProductRequest request) {
        tagService.createTagProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/product")
    public ResponseEntity tagProductDisconnect(@Valid @RequestBody CreateDeleteTagProductRequest request) {
        tagService.deleteTagProduct(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
