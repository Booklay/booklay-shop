package com.nhnacademy.booklay.server.service.storage.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.nhnacademy.booklay.server.dto.stroage.request.FileRequest;
import com.nhnacademy.booklay.server.dto.stroage.request.FileResolveRequest;
import com.nhnacademy.booklay.server.dto.stroage.response.ObjectFileResponse;
import com.nhnacademy.booklay.server.repository.ObjectFileRepository;
import com.nhnacademy.booklay.server.service.storage.StorageService;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {

    @Mock
    StorageService storageService;

    @Mock
    ObjectFileRepository objectFileRepository;

    @InjectMocks
    FileServiceImpl fileService;

    MultipartFile file;

    @BeforeEach
    void setUp(){
        file = new MockMultipartFile(
            "file",
            "file.txt",
            "test/txt",
            "multipart".getBytes());
    }

    @Test
    void uploadFile() throws IOException {

        given(storageService.uploadImage( eq(file), any(FileResolveRequest.class)))
            .willReturn(new FileRequest());

        fileService.uploadFile(file);

        then(objectFileRepository).should(times(1)).save(any());
    }

    @Test
    void downloadFile() throws IOException {

        ObjectFileResponse response =
            new ObjectFileResponse(1L,"name","address/");

        given(objectFileRepository.queryById(1L)).willReturn(
           response);

        fileService.downloadFile(1L);

        then(storageService).should(times(1)).downloadFile(response);
    }

    @Test
    void downloadUrl() {

        ObjectFileResponse response =
            new ObjectFileResponse(1L,"name","address/");

        given(objectFileRepository.queryById(1L)).willReturn(
            response);

        String url = fileService.downloadUrl(1L);
        String expect = response.getFileAddress() + response.getFileName();

        assertThat(url).isEqualTo(expect);
    }
}