package com.oli.weather.exception.user;

import com.oli.weather.exception.ApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ResourceAlreadyExists extends ApplicationException {

    public ResourceAlreadyExists(String additionalMessage, String returnPage) {
        super(additionalMessage, HttpStatus.CONFLICT, returnPage);
    }
}
