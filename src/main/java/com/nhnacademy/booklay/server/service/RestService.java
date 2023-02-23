package com.nhnacademy.booklay.server.service;


import com.nhnacademy.booklay.server.dto.ApiEntity;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class RestService {

    private final RestTemplate restTemplate;


    public <T> ApiEntity<T> post(String url, Map<String, Object> requestBody,
                                 Class<T> responseType) {
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody);
        ApiEntity<T> apiEntity = new ApiEntity<>();
        ResponseEntity<T> response =
            restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
        apiEntity.setSuccessResponse(response);
        return apiEntity;
    }

    public <T> ApiEntity<T> post(String url, MultiValueMap<String, String> header,  Map<String, Object> requestBody,
                                 Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.addAll(header);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ApiEntity<T> apiEntity = new ApiEntity<>();
        ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
        apiEntity.setSuccessResponse(response);
        return apiEntity;
    }


    public <T> ApiEntity<T> put(String url, Map<String, Object> requestBody,
                                Class<T> responseType) {

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody);
        ApiEntity<T> apiEntity = new ApiEntity<>();
        ResponseEntity<T> response =
            restTemplate.exchange(url, HttpMethod.PUT, entity, responseType);
        apiEntity.setSuccessResponse(response);
        return apiEntity;
    }

    public <T> ApiEntity<T> get(String url, MultiValueMap<String, String> params,
                                ParameterizedTypeReference<T> responseType) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParams(params);
        ApiEntity<T> apiEntity = new ApiEntity<>();
        ResponseEntity<T> response =
            restTemplate.exchange(builder.build().toUriString(), HttpMethod.GET, null,
                responseType);
        apiEntity.setSuccessResponse(response);
        return apiEntity;
    }

    public <T> ApiEntity<T> get(String url, MultiValueMap<String, String> params,
                                Class<T> responseType) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParams(params);
        ApiEntity<T> apiEntity = new ApiEntity<>();
        ResponseEntity<T> response =
            restTemplate.exchange(builder.build().toUriString(), HttpMethod.GET, null,
                responseType);
        apiEntity.setSuccessResponse(response);

        return apiEntity;
    }

    public void delete(String url) {
        restTemplate.delete(url);
    }

    public void delete(String url, Map<String, Object> requestBody){
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody);
        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }

    public void delete(String url, MultiValueMap<String, String> params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParams(params);
        restTemplate.delete(builder.build().toUriString());
    }

    /**
     * @param <T> 인자 값에 들어가는 제네릭 선언 지우지 말고 채워넣어야 제대로된 타입이 넘어옵니다.
     *  인텔리제이에
     *   List<String> list = new ArrayList<String>(); // reports array list type argument
     * After the quick-fix is applied:
     *   List<String> list = new ArrayList<>();
     */

    public <T> ApiEntity<T> post(String url, Map<String, Object> requestBody,
                                 MultiValueMap<String, String> params,
                                 ParameterizedTypeReference<T> responseType) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParams(params);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody);

        ApiEntity<T> apiEntity = new ApiEntity<>();
        ResponseEntity<T> response =
            restTemplate.exchange(builder.build().toUriString(), HttpMethod.POST, entity, responseType);
        apiEntity.setSuccessResponse(response);

        return apiEntity;
    }

    public <T> ApiEntity<T> post(String url, Map<String, Object> requestBody,
                                 ParameterizedTypeReference<T> responseType) {

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody);

        ApiEntity<T> apiEntity = new ApiEntity<>();
        ResponseEntity<T> response =
            restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
        apiEntity.setSuccessResponse(response);

        return apiEntity;
    }
}
