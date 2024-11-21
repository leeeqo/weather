package com.oli.weather.controller;

import com.oli.weather.dto.UserDTO;
import com.oli.weather.exception.ApplicationException;
import com.oli.weather.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import static com.oli.weather.utils.ValidationUtils.validateLoginAndPassword;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/sign-in")
    public String signInPage() {
        System.out.println("Get sign=in");

        return "sign-in";
    }

    @PostMapping("/sign-in")
    public String signIn(@ModelAttribute("user") UserDTO userDTO, HttpServletResponse response) {
        try {
            validateLoginAndPassword(userDTO);

            Integer sessionId = authService.getSessionId(userDTO);

            response.addCookie(new Cookie("sessionId", sessionId.toString()));
        } catch (ApplicationException e) {
            //TODO
        }

        // TODO
        return "home";
    }

    @GetMapping("/sign-up")
    public String signUpPage() {
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(@ModelAttribute("user") UserDTO userDTO, HttpServletResponse response) {
        try {
            validateLoginAndPassword(userDTO);

            Integer sessionId = authService.getSignUpSessionId(userDTO);

            response.addCookie(new Cookie("sessionId", sessionId.toString()));

        } catch (ApplicationException e) {
            // TODO
        }

        // TODO
        return "index";
    }
}
