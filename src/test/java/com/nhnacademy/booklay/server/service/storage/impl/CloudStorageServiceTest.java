package com.nhnacademy.booklay.server.service.storage.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.server.dto.stroage.request.FileResolveRequest;
import com.nhnacademy.booklay.server.dto.stroage.response.ObjectFileResponse;
import com.nhnacademy.booklay.server.dto.stroage.response.auth.Access;
import com.nhnacademy.booklay.server.dto.stroage.response.auth.AccessResponse;
import com.nhnacademy.booklay.server.dto.stroage.response.auth.Token;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class CloudStorageServiceTest {


    @Mock
    RestTemplate restTemplate;

    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    CloudStorageService storageService;

    MultipartFile file;

    FileResolveRequest request;


    AccessResponse accessResponse;

    @BeforeEach
    void setUp(){
        file = new MockMultipartFile(
            "file",
            "file.txt",
            "test/txt",
            "multipart".getBytes());

        request = FileResolveRequest.builder()
            .fileExtension("txt")
            .fileType("text")
            .build();

        Token token = new Token();
        ReflectionTestUtils.setField(token,"id","tokenId");

        Access access = new Access();
        ReflectionTestUtils.setField(access,"token",token);

        accessResponse = new AccessResponse();
        ReflectionTestUtils.setField(accessResponse,"access",access);


    }

//    @Test
    void uploadImage() throws IOException {

        ResponseEntity<String> response =
            new ResponseEntity<>(accessResponse.toString(),HttpStatus.OK);


        given(
            restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class) , eq(String.class)))
            .willReturn(response);

        given(
            objectMapper.readValue(response.getBody(), AccessResponse.class)
        ).willReturn(accessResponse);

        given(
            restTemplate.execute(anyString(), eq(HttpMethod.PUT), any(RequestCallback.class) , any(ResponseExtractor.class)))
            .willReturn(response);

        storageService.uploadImage(file,request);

    }

    @Test
    void downloadFile() throws IOException {

        ObjectFileResponse objectFileResponse =
            new ObjectFileResponse(1L,"name","address/");

        ResponseEntity<String> response =
            new ResponseEntity<>(accessResponse.toString(),HttpStatus.OK);

        given(restTemplate
                .exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class) , eq(String.class)))
            .willReturn(response);

        given(objectMapper.readValue(response.getBody(), AccessResponse.class))
            .willReturn(accessResponse);

        storageService.downloadFile(objectFileResponse);

        then(restTemplate).should()
            .exchange( anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(byte[].class));

    }
}