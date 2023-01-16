package com.nhnacademy.booklay.server.exception;

public class ImageNotFoundException extends RuntimeException {

    private static final String ERROR = "이미지를 찾을 수 없습니다.";

    public ImageNotFoundException() {
        super(ERROR);
    }

}
