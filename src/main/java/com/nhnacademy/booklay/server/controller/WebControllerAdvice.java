package com.nhnacademy.booklay.server.controller;

import com.nhnacademy.booklay.server.exception.category.NotFoundException;
import com.nhnacademy.booklay.server.exception.category.CreateCategoryFailedException;
import com.nhnacademy.booklay.server.exception.category.UpdateCategoryFailedException;
import com.nhnacademy.booklay.server.exception.category.ValidationFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class WebControllerAdvice {

    @ExceptionHandler(CreateCategoryFailedException.class)
    @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "카테고리 생성 실패.")
    public String createCategoryFailedException(Exception ex) {
        return treatException(ex);
    }

    @ExceptionHandler(UpdateCategoryFailedException.class)
    @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "카테고리 수정 실패.")
    public String updateCategoryFailedException(Exception ex) {
        return treatException(ex);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "존재하지 않는 카테고리에 대한 요청입니다.")
    public String categoryNotFoundException(Exception ex) {
        return treatException(ex);
    }

    @ExceptionHandler(ValidationFailedException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "필수 입력 값이 없거나 잘못된 값이 존재합니다.")
    public String invalidRequestException(Exception ex) {
        return treatException(ex);
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex) {
        return treatException(ex);
    }

    private String treatException(Exception ex) {
        printInfo(ex);
        return printError(ex);
    }

    private void printInfo(Exception ex) {
        log.info("Exception Class : " + ex.getClass());
    }

    private String printError(Exception ex) {
        log.error("Exception Message : " + ex.getMessage());
        return ex.getMessage();
    }
}
