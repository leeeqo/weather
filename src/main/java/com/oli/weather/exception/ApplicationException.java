package com.oli.weather.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationException extends RuntimeException {

    private final String additionalMessage;
    private final HttpStatus httpStatus;
    private final String returnPage;

    public ApplicationException(String additionalMessage) {
        this.additionalMessage = additionalMessage;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        this.returnPage = "/home";
    }

    public ApplicationException(String additionalMessage, HttpStatus httpStatus) {
        this.additionalMessage = additionalMessage;
        this.httpStatus = httpStatus;
        this.returnPage = "/home";
    }

    public ApplicationException(String additionalMessage, HttpStatus httpStatus, String returnPage) {
        this.additionalMessage = additionalMessage;
        this.httpStatus = httpStatus;
        this.returnPage = returnPage;
    }
}
