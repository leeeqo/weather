package com.oli.weather.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ApplicationException extends RuntimeException {

    private String message;
}
