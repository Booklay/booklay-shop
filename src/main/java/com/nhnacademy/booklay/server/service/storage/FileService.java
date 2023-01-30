package com.nhnacademy.booklay.server.service.storage;

import com.nhnacademy.booklay.server.dto.stroage.request.FileResolveRequest;
import com.nhnacademy.booklay.server.entity.ObjectFile;
import java.io.IOException;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    ObjectFile uploadFile(@RequestPart MultipartFile file)
        throws IOException;

    ObjectFile uploadFileResolve(final MultipartFile image, FileResolveRequest fileResolveRequest)
        throws IOException;

    String downloadFile(final Long id) throws IOException;
}
