package com.oli.weather.exception.user;

import com.oli.weather.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class AuthorizationException extends ApplicationException {

    public AuthorizationException(String additionalMessage, HttpStatus httpStatus, String returnPage) {
        super(additionalMessage, httpStatus, returnPage);
    }
}
