package com.nhnacademy.booklay.server.controller.admin.product;

import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.product.author.request.CreateAuthorRequest;
import com.nhnacademy.booklay.server.dto.product.author.request.UpdateAuthorRequest;
import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.dto.product.request.CreateProductBookRequest;
import com.nhnacademy.booklay.server.dto.product.request.CreateProductSubscribeRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.CreateTagRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.CreateDeleteTagProductRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.UpdateTagRequest;
import com.nhnacademy.booklay.server.dto.product.tag.response.RetrieveTagResponse;
import com.nhnacademy.booklay.server.dto.product.tag.response.TagProductResponse;
import com.nhnacademy.booklay.server.service.product.AuthorService;
import com.nhnacademy.booklay.server.service.product.ProductService;
import com.nhnacademy.booklay.server.service.product.TagService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author 최규태
 */


@Slf4j
@RestController
@RequestMapping("/admin/product")
@RequiredArgsConstructor
public class ProductAdminController {

    private final ProductService productService;
    private final TagService tagService;
    private final AuthorService authorService;

    //책 등록
    @PostMapping(value = "/register/book",
        consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE})
    public Long postBookRegister(
        @RequestPart CreateProductBookRequest request,
        @RequestPart MultipartFile imgFile) throws Exception {
        //product
        log.info("이미지 시험 출력 : " + imgFile.getContentType() + imgFile.getOriginalFilename());
        request.setImage(imgFile);
        return productService.createBookProduct(request);
    }

    //책 수정
    @PutMapping("/register/book")
    public Long postBookUpdater(CreateProductBookRequest request) throws Exception {

        return productService.updateBookProduct(request);
    }


    //구독
    @PostMapping("/register/subscribe")
    public Long postSubscribeRegister(CreateProductSubscribeRequest request) throws Exception {
        return productService.createSubscribeProduct(request);
    }

    //구독 수정
    @PutMapping("/update/subscribe")
    public Long postSubscribeUpdate(CreateProductSubscribeRequest request) throws Exception {
        return productService.updateSubscribeProduct(request);
    }


    //태그 자체만
    @GetMapping("/tag")
    public PageResponse<RetrieveTagResponse> tagPage(Pageable pageable) {
        Page<RetrieveTagResponse> response = tagService.retrieveAllTag(pageable);
        return new PageResponse<>(response);
    }

    @PostMapping("/tag")
    public void tagRegister(@Valid @RequestBody CreateTagRequest request) {
        tagService.createTag(request);
    }

    @PutMapping("/tag")
    public void tagUpdate(@Valid @RequestBody UpdateTagRequest request) {
        tagService.updateTag(request);
    }

    @DeleteMapping("/tag")
    public void tagDelete(Long id) {
        tagService.deleteTag(id);
    }


    //작가
    @GetMapping("/author")
    public PageResponse<RetrieveAuthorResponse> authorPage(Pageable pageable) {
        Page<RetrieveAuthorResponse> response = authorService.retrieveAllAuthor(pageable);
        return new PageResponse<>(response);
    }

    @PostMapping("/author")
    public void authorRegister(@Valid @RequestBody CreateAuthorRequest request) {
        authorService.createAuthor(request);
    }

    @PutMapping("/author")
    public void authorUpdate(@Valid @RequestBody UpdateAuthorRequest request) {
        authorService.updateAuthor(request);
    }

    @DeleteMapping("/author")
    public void authorDelete(Long id) {
        authorService.deleteAuthor(id);
    }

    //태그-작품 연동
    @GetMapping("/tag/product/{productNo}")
    public PageResponse<TagProductResponse> tagProductPage(Pageable pageable,
        @PathVariable Long productNo){

        log.info("상품 번호 : " + productNo);

        Page<TagProductResponse> response =  tagService.retrieveAllTagWithBoolean(pageable, productNo);
        return new PageResponse<>(response);
    }

    //태그 작품 연동 생성
    @PostMapping("/tag/product")
    public void tagProductConnect(@Valid @RequestBody CreateDeleteTagProductRequest request){
        log.info("출력 : "+request.getProductNo());
        tagService.connectTagProduct(request);
    }

    @DeleteMapping("/tag/product")
    public void tagProductDisconnect(@Valid @RequestBody CreateDeleteTagProductRequest request){
        log.info("출력 : "+request.getProductNo());
        tagService.disconnectTagProduct(request);
    }
}
