package com.nhnacademy.booklay.server.controller.admin.product;

import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.product.author.request.CreateAuthorRequest;
import com.nhnacademy.booklay.server.dto.product.author.request.UpdateAuthorRequest;
import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.service.product.AuthorService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

  //작가
  @GetMapping
  public PageResponse<RetrieveAuthorResponse> authorPage(Pageable pageable) {
    Page<RetrieveAuthorResponse> response = authorService.retrieveAllAuthor(pageable);
    return new PageResponse<>(response);
  }

  @PostMapping
  public void authorRegister(@Valid @RequestBody CreateAuthorRequest request) {
    authorService.createAuthor(request);
  }

  @PutMapping
  public void authorUpdate(@Valid @RequestBody UpdateAuthorRequest request) {
    authorService.updateAuthor(request);
  }

  @DeleteMapping
  public void authorDelete(Long id) {
    authorService.deleteAuthor(id);
  }

}
