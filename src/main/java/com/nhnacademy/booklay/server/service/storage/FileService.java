package com.nhnacademy.booklay.server.service.storage;

import com.nhnacademy.booklay.server.dto.stroage.request.FileResolveRequest;
import com.nhnacademy.booklay.server.dto.stroage.response.ObjectFileResponse;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    void uploadFile(final MultipartFile image) throws IOException;

    ObjectFileResponse uploadFile(final MultipartFile image, FileResolveRequest fileResolveRequest)
        throws IOException;

    String downloadFile(final Long id) throws IOException;
}
