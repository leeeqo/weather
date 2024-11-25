package com.oli.weather.exception.user;

import com.oli.weather.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends ApplicationException {

    public NotFoundException(String additionalMessage) {
        super(additionalMessage, HttpStatus.NOT_FOUND);
    }

    public NotFoundException(String additionalMessage, String returnPage) {
        super(additionalMessage, HttpStatus.NOT_FOUND, returnPage);
    }
}
