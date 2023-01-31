package com.nhnacademy.booklay.server.service.storage.impl;

import static org.springframework.http.HttpMethod.PUT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.server.dto.stroage.request.FileRequest;
import com.nhnacademy.booklay.server.dto.stroage.request.FileResolveRequest;
import com.nhnacademy.booklay.server.dto.stroage.request.auth.AccessRequest;
import com.nhnacademy.booklay.server.dto.stroage.request.auth.Auth;
import com.nhnacademy.booklay.server.dto.stroage.request.auth.PasswordCredentials;
import com.nhnacademy.booklay.server.dto.stroage.response.ObjectFileResponse;
import com.nhnacademy.booklay.server.dto.stroage.response.auth.AccessResponse;
import com.nhnacademy.booklay.server.service.storage.StorageService;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class CloudStorageService implements StorageService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private AccessResponse accessResponse;

    @Value("${cloud.auth-url}")
    private String authUrl;
    @Value("${cloud.user-name}")
    private String userName;
    @Value("${cloud.password}")
    private String password;
    @Value("${cloud.tenant-id}")
    private String tenantId;
    @Value("${cloud.storage-url}")
    private String storageUrl;
    @Value("${cloud.container}")
    private String container;

    private static final String HEADER_NAME = "X-Auth-Token";
    private static final String DIR = System.getProperty("java.io.tmpdir");
    private static final String LOCAL_DIR = System.getProperty("user.home");

    public String requestToken() {
        PasswordCredentials passwordCredentials = new PasswordCredentials(userName, password);
        Auth auth = new Auth(tenantId, passwordCredentials);
        AccessRequest accessRequest = new AccessRequest(auth);

        String identityUrl = this.authUrl + "/tokens";

        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<AccessRequest> httpEntity = new HttpEntity<>(accessRequest, headers);

        // 토큰 요청
        ResponseEntity<String> response =
            this.restTemplate.exchange(identityUrl, HttpMethod.POST, httpEntity, String.class);

        return response.getBody();
    }

    @Override
    public FileRequest uploadImage(final MultipartFile file, FileResolveRequest fileResolveRequest)
        throws IOException {
        String fileName = UUID.randomUUID() + "." + fileResolveRequest.getFileExtension();

        String fileAddress =
            this.storageUrl + this.container + fileResolveRequest.getFileType() + "/";

        File tempFileForUpload = new File(DIR, fileName);

        file.transferTo(tempFileForUpload);

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setBufferRequestBody(false);
        RestTemplate requestRestTemplate = new RestTemplate(requestFactory);

        HttpMessageConverterExtractor<String> responseExtractor =
            new HttpMessageConverterExtractor<>(String.class,
                                                requestRestTemplate.getMessageConverters());

        try (InputStream inputStream = new FileInputStream(tempFileForUpload)) {
            accessResponse = objectMapper.readValue(requestToken(), AccessResponse.class);
            String authTokenId = accessResponse.getAccess().getToken().getId();

            requestRestTemplate.execute(fileAddress + fileName, PUT,
                                        request -> {
                                            request.getHeaders().add(HEADER_NAME, authTokenId);
                                            IOUtils.copy(inputStream, request.getBody());
                                        }, responseExtractor);

            log.info("File Upload Success , fileName: {}", fileName);
        } catch (IOException e) {
            log.error("error: {}", e.getMessage());
            throw e;
        }

        return FileRequest.builder()
                          .fileAddress(fileAddress)
                          .fileName(fileName)
                          .build();
    }

    @Override
    public ResponseEntity<byte[]> downloadFile(ObjectFileResponse fileResponse) throws IOException {

        accessResponse = objectMapper.readValue(requestToken(), AccessResponse.class);
        String authTokenId = accessResponse.getAccess().getToken().getId();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_NAME, authTokenId);
        headers.setAccept(List.of(MediaType.APPLICATION_OCTET_STREAM));

        HttpEntity<String> requestHttpEntity = new HttpEntity<>(null, headers);

        String fileName = fileResponse.getFileName();
        String url = fileResponse.getFileAddress() + fileName;

        return this.restTemplate.exchange(url, HttpMethod.GET, requestHttpEntity, byte[].class);
    }

    private Path getDownloadDir() {
        return Paths.get(LOCAL_DIR, "/Downloads");
    }
}
