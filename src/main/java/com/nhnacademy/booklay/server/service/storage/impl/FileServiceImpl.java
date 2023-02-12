package com.nhnacademy.booklay.server.service.storage.impl;

import com.nhnacademy.booklay.server.dto.stroage.request.FileRequest;
import com.nhnacademy.booklay.server.dto.stroage.request.FileResolveRequest;
import com.nhnacademy.booklay.server.dto.stroage.response.ObjectFileResponse;
import com.nhnacademy.booklay.server.entity.ObjectFile;
import com.nhnacademy.booklay.server.repository.ObjectFileRepository;
import com.nhnacademy.booklay.server.service.storage.FileService;
import com.nhnacademy.booklay.server.service.storage.StorageService;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {

    private final StorageService storageService;
    private final ObjectFileRepository objectFileRepository;


    @Override
    public ObjectFile uploadFile(@RequestPart final MultipartFile file)
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

            return uploadFileResolve(file, fileResolveRequest);
        }

        throw new IOException("파일이 손상되었거나 지원하지 않는 형식입니다.");
    }

    private ObjectFile uploadFileResolve(MultipartFile image, FileResolveRequest fileResolveRequest)
        throws IOException {
        FileRequest request = storageService.uploadImage(image, fileResolveRequest);

        return objectFileRepository.save(
            ObjectFile.builder()
                      .fileAddress(request.getFileAddress())
                      .fileName(request.getFileName())
                      .build()
        );
    }

    @Override
    public ResponseEntity<byte[]> downloadFile(final Long id) throws IOException {
        ObjectFileResponse response = objectFileRepository.queryById(id);

        return storageService.downloadFile(response);
    }

    @Override
    public String downloadUrl(Long id){
        ObjectFileResponse response = objectFileRepository.queryById(id);
        return response.getFileAddress() + response.getFileName();
    }
}
