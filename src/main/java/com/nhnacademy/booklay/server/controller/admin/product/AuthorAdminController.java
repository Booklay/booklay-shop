package com.nhnacademy.booklay.server.controller.admin.product;

import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.product.DeleteIdRequest;
import com.nhnacademy.booklay.server.dto.product.author.request.CreateAuthorRequest;
import com.nhnacademy.booklay.server.dto.product.author.request.UpdateAuthorRequest;
import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.service.product.AuthorService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
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
@RequestMapping("/admin/author")
@RequiredArgsConstructor
public class AuthorAdminController {

    private final AuthorService authorService;

    // 작가
    @GetMapping
    public ResponseEntity<PageResponse<RetrieveAuthorResponse>> authorPage(Pageable pageable) {
        Page<RetrieveAuthorResponse> response = authorService.retrieveAllAuthor(pageable);
        PageResponse<RetrieveAuthorResponse> result = new PageResponse<>(response);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{authorNo}")
    public ResponseEntity<RetrieveAuthorResponse> authorEdit(@PathVariable Long authorNo){
        RetrieveAuthorResponse result = authorService.retrieveAuthorForUpdate(authorNo);
        return ResponseEntity.status(HttpStatus.OK)
            .body(result);
    }

    @PostMapping
    public ResponseEntity<Void> authorRegister(@Valid @RequestBody CreateAuthorRequest request) {
        authorService.createAuthor(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Void> authorUpdate(@Valid @RequestBody UpdateAuthorRequest request) {
       authorService.updateAuthor(request);
       return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> authorDelete(@Valid @RequestBody DeleteIdRequest request) {
        authorService.deleteAuthor(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
