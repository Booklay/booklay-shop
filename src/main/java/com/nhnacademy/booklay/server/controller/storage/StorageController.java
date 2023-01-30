package com.nhnacademy.booklay.server.controller.storage;

import com.nhnacademy.booklay.server.dto.stroage.request.FileResolveRequest;
import com.nhnacademy.booklay.server.service.storage.FileService;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
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
    public ResponseEntity<Void> uploadFile(@RequestPart final MultipartFile file)
        throws IOException {

        Optional<String> contentType =
            Arrays.stream(Objects.requireNonNull(file.getContentType()).split("/")).findFirst();

        Optional<String> originalFilename = Optional.ofNullable(file.getOriginalFilename());
        Optional<String> fileExtension = Optional.empty();

        if (originalFilename.isPresent()) {
            fileExtension = originalFilename.filter(f -> f.contains("."))
                                            .map(f -> f.substring(
                                                originalFilename.get().lastIndexOf(".") + 1));
        }

        if (contentType.isPresent() && fileExtension.isPresent()) {
            FileResolveRequest fileResolveRequest = FileResolveRequest.builder()
                                                                      .fileExtension(
                                                                          fileExtension.get())
                                                                      .fileType(contentType.get())
                                                                      .build();

            fileService.uploadFileResolve(file, fileResolveRequest);

            return ResponseEntity.status(HttpStatus.CREATED)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .build();

        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .contentType(MediaType.APPLICATION_JSON)
                             .build();
    }

    /**
     * Object Storage Download.
     *
     * @param fileId 파일이 저장된 주소를 가지고 있는 튜플의 ID
     * @return 저장된 파일 위치
     * @throws IOException .
     */

    @GetMapping("/{fileId}")
    public ResponseEntity<String> downloadFile(@PathVariable("fileId") final Long fileId)
        throws IOException {
        String path = fileService.downloadFile(fileId);

        return ResponseEntity.status(HttpStatus.OK)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(path);
    }
}
