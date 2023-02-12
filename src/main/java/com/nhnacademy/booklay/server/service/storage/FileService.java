package com.nhnacademy.booklay.server.service.storage;

import com.nhnacademy.booklay.server.entity.ObjectFile;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    ObjectFile uploadFile(@RequestPart MultipartFile file)
        throws IOException;

    ResponseEntity<byte[]> downloadFile(final Long id) throws IOException;

    String downloadUrl(Long id);
}
