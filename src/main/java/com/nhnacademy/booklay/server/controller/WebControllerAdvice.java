package com.nhnacademy.booklay.server.controller;

import com.nhnacademy.booklay.server.exception.controller.CreateFailedException;
import com.nhnacademy.booklay.server.exception.controller.DeleteFailedException;
import com.nhnacademy.booklay.server.exception.controller.UpdateFailedException;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class WebControllerAdvice {

    @ExceptionHandler(value = {
        CreateFailedException.class,
        UpdateFailedException.class,
        DeleteFailedException.class,
        NotFoundException.class
    })
    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "잘못된 요청 정보")
    public String createFailedException(Exception ex) {
        return treatException(ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "필수 입력 값이 없거나 잘못된 값이 존재합니다.")
    public String invalidRequestException(Exception ex) {
        return treatException(ex);
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex) {
        return treatException(ex);
    }

    private String treatException(Exception ex) {
        log.error("Exception Class : " + ex.getClass());
        log.error("Exception Message : " + ex.getMessage());
        return ex.getMessage();
    }
}
