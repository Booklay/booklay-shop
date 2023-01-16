package com.nhnacademy.booklay.server.dto.stroage.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FileResolveRequest {

    String fileExtension;
    String fileType;

    @Builder
    public FileResolveRequest(String fileExtension, String fileType) {
        this.fileExtension = fileExtension;
        this.fileType = fileType;
    }
}
