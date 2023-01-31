package com.nhnacademy.booklay.server.service.storage;

import com.nhnacademy.booklay.server.dto.stroage.request.FileRequest;
import com.nhnacademy.booklay.server.dto.stroage.request.FileResolveRequest;
import com.nhnacademy.booklay.server.dto.stroage.response.ObjectFileResponse;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

  FileRequest uploadImage(MultipartFile file, FileResolveRequest fileResolveRequest)
      throws IOException;

  ResponseEntity<byte[]> downloadFile(final ObjectFileResponse image) throws IOException;
}
