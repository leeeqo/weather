package com.oli.weather.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ApplicationException.class)
    public String handleApplicationException(ApplicationException ex,
                                           Model model,
                                           HttpServletResponse response) throws IOException {

        //redirectAttributes.addAttribute("errorMessage", ex.getAdditionalMessage());
        //redirectAttributes.addAttribute("errorStatus", ex.getHttpStatus());
        model.addAttribute("errorMessage", ex.getAdditionalMessage());
        model.addAttribute("errorStatus", ex.getHttpStatus().value());
        model.addAttribute("returnPage", ex.getReturnPage());

        response.setStatus(ex.getHttpStatus().value());

        return "error";

        //response.setStatus(ex.getHttpStatus().value());
        //response.sendRedirect("/weather/sign-in");

        //log.info("ReturnedPage = ");
        //return new ModelAndView("sign-in");
    }
}
