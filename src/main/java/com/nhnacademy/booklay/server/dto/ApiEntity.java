package com.nhnacademy.booklay.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class
ApiEntity<T> {
    private ResponseEntity<T> successResponse;
    private HttpClientErrorException httpClientErrorException;

    public T getBody() {
        return successResponse.getBody();
    }

    public boolean isSuccess() {
        return successResponse != null &&
            (
                successResponse.getStatusCode() == HttpStatus.OK
                    || successResponse.getStatusCode() == HttpStatus.CREATED
            );
    }

}
