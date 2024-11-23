package com.oli.weather.controller;

import lombok.extern.slf4j.Slf4j;
import com.oli.weather.dto.UserDTO;
import com.oli.weather.exception.ApplicationException;
import com.oli.weather.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

import static com.oli.weather.utils.RequestUtils.getSessionCookie;
import static com.oli.weather.utils.ValidationUtils.validateLoginAndPassword;

@Slf4j
@Controller
public class AuthController {

    private static final String REDIRECT_HOME = "/weather/home";

    @Autowired
    private AuthService authService;

    @GetMapping("/sign-in")
    public String signInPage() {
        return "sign-in";
    }

    @PostMapping("/sign-in")
    public void signIn(@ModelAttribute("user") UserDTO userDTO, HttpServletResponse response) throws IOException {
        try {
            validateLoginAndPassword(userDTO);

            Integer sessionId = authService.verifyUser(userDTO);

            log.info("User " + userDTO.getLogin() + " is verified");

            response.addCookie(new Cookie("sessionId", sessionId.toString()));
        } catch (ApplicationException e) {
            //TODO
        }

        response.sendRedirect(REDIRECT_HOME);
    }

    @GetMapping("/sign-up")
    public String signUpPage() {
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public void signUp(@ModelAttribute("user") UserDTO userDTO, HttpServletResponse response) throws IOException {
        try {
            validateLoginAndPassword(userDTO);

            Integer sessionId = authService.registerUser(userDTO);

            log.info("User " + userDTO.getLogin() + " is registered.");

            response.addCookie(new Cookie("sessionId", sessionId.toString()));
        } catch (ApplicationException e) {
            // TODO
        }

        response.sendRedirect(REDIRECT_HOME);
    }

    @PostMapping("/sign-out")
    public void signOut(@ModelAttribute("user") UserDTO userDTO,
                        HttpServletRequest request,
                        HttpServletResponse response) throws IOException {

        String sessionId = getSessionCookie(request);

        if (sessionId == null) {
            // TODO
            throw new ApplicationException("Not authorized. Impossible to sign out");
        }

        authService.removeSession(sessionId);

        response.addCookie(new Cookie("sessionId", null));

        response.sendRedirect(REDIRECT_HOME);
    }
}
