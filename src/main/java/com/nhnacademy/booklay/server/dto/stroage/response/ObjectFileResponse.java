package com.nhnacademy.booklay.server.dto.stroage.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ObjectFileResponse {
    private final Long id;
    private final String fileName;
    private final String fileAddress;
}
