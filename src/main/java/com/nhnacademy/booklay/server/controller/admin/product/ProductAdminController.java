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


    //책 등록
    @PostMapping(value = "/books",
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
    @PutMapping("/books")
    public Long postBookUpdater(CreateProductBookRequest request) throws Exception {

        return productService.updateBookProduct(request);
    }


    //구독
    @PostMapping("/subscribes")
    public Long postSubscribeRegister(CreateProductSubscribeRequest request) throws Exception {
        return productService.createSubscribeProduct(request);
    }

    //구독 수정
    @PutMapping("/subscribes")
    public Long postSubscribeUpdate(CreateProductSubscribeRequest request) throws Exception {
        return productService.updateSubscribeProduct(request);
    }

}
