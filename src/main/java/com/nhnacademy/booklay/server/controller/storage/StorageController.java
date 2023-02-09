package com.nhnacademy.booklay.server.controller.storage;

import com.nhnacademy.booklay.server.entity.ObjectFile;
import com.nhnacademy.booklay.server.service.storage.FileService;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/storage")
public class StorageController {

  private final FileService fileService;

  public StorageController(FileService fileService) {
    this.fileService = fileService;
  }

  /**
   * Object Storage Upload.
   *
   * @param file html input type = file 등으로 첨부파일 선택.
   * @return DB에 저장된 ID, 디렉토리를 포함한 주소, 파일 이름.
   * @throws IOException .
   */

  @PostMapping
  public ResponseEntity<ObjectFile> uploadFile(@RequestPart final MultipartFile file)
      throws IOException {
    ObjectFile objectFile = fileService.uploadFile(file);

    return ResponseEntity.status(HttpStatus.CREATED)
        .contentType(MediaType.APPLICATION_JSON)
        .body(objectFile);
  }

  /**
   * Object Storage Download.
   *
   * @param fileId 파일이 저장된 주소를 가지고 있는 튜플의 ID
   * @return 저장된 파일 위치
   * @throws IOException .
   */

  @GetMapping("/{fileId}")
  public ResponseEntity<String> downloadUrl(@PathVariable("fileId") final Long fileId)
      throws IOException {
    return ResponseEntity.status(HttpStatus.OK)
        .body(fileService.downloadUrl(fileId));
  }

  //    @GetMapping("/ebook/{fileId}")
  public ResponseEntity<byte[]> downloadEBook(@PathVariable("fileId") final Long fileId)
      throws IOException {
    return fileService.downloadFile(fileId);
  }

}
