package com.oli.weather.exception.user;

import com.oli.weather.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class ResourceAlreadyExists extends ApplicationException {

    public ResourceAlreadyExists(String additionalMessage, String returnPage) {
        super(additionalMessage, HttpStatus.CONFLICT, returnPage);
    }
}
