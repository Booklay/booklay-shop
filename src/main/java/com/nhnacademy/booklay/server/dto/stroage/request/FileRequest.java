package com.nhnacademy.booklay.server.dto.stroage.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FileRequest {
    private String fileAddress;
    private String fileName;

    @Builder
    private FileRequest(String fileAddress, String fileName) {
        this.fileAddress = fileAddress;
        this.fileName = fileName;
    }
}
