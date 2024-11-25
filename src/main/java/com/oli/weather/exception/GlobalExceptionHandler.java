package com.oli.weather.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ApplicationException.class)
    public String handleApplicationException(ApplicationException ex,
                                             Model model,
                                             HttpServletResponse response) throws IOException {

        log.info(ex.getMessage());

        model.addAttribute("errorMessage", ex.getAdditionalMessage());
        model.addAttribute("errorStatus", ex.getHttpStatus().value());
        model.addAttribute("returnPage", ex.getReturnPage());

        response.setStatus(ex.getHttpStatus().value());

        return "error";
    }

    @ExceptionHandler(value = Exception.class)
    public String handleException(Exception ex,
                                  Model model,
                                  HttpServletResponse response) {

        log.info(ex.getMessage());

        model.addAttribute("errorMessage", "Unknown exception was thrown");
        model.addAttribute("returnPage", "/home");

        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        return "error";
    }
}
