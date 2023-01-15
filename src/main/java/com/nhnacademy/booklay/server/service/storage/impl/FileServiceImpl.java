package com.nhnacademy.booklay.server.service.storage.impl;

import com.nhnacademy.booklay.server.dto.stroage.request.FileRequest;
import com.nhnacademy.booklay.server.dto.stroage.request.FileResolveRequest;
import com.nhnacademy.booklay.server.dto.stroage.response.ObjectFileResponse;
import com.nhnacademy.booklay.server.entity.ObjectFile;
import com.nhnacademy.booklay.server.repository.ObjectFileRepository;
import com.nhnacademy.booklay.server.service.storage.FileService;
import com.nhnacademy.booklay.server.service.storage.StorageService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {
    
    private final StorageService storageService;
    private final ObjectFileRepository objectFileRepository;

    public void uploadFile(final MultipartFile image) throws IOException {
        FileRequest request = storageService.uploadImage(image);

        objectFileRepository.save(
            ObjectFile.builder()
                .fileAddress(request.getFileAddress())
                .build()
        );
    }

    @Override
    public ObjectFileResponse uploadFile(MultipartFile image, FileResolveRequest fileResolveRequest)
        throws IOException {
        FileRequest request = storageService.uploadImage(image, fileResolveRequest);

        ObjectFile objectFile = objectFileRepository.save(
            ObjectFile.builder()
                .fileAddress(request.getFileAddress())
                .fileName(request.getFileName())
                .build()
        );

        return new ObjectFileResponse(
            objectFile.getId(),
            objectFile.getFileName(),
            objectFile.getFileAddress()
        );
    }

    @Override
    public String downloadFile(final Long id) throws IOException {
        ObjectFileResponse response = objectFileRepository.queryById(id);

        return storageService.downloadFile(response);
    }
}
