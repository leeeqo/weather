package com.oli.weather.utils;

import com.oli.weather.entity.User;
import com.oli.weather.exception.user.AuthorizationException;
import org.springframework.http.HttpStatus;

public class ValidationUtils {

    public static void validateLoginAndPassword(User user, String returnPage) {
        if (!validateLogin(user.getLogin())) {
            throw new AuthorizationException("Login must be provided", HttpStatus.BAD_REQUEST, returnPage);
        };

        if (!validatePassword(user.getPassword())) {
            throw new AuthorizationException("Password should contain:\n" +
                    "1 number,\n" +
                    "1 uppercase letter,\n" +
                    "1 lowercase letter.\n" +
                    "Password should be 8-16 characters with no space.", HttpStatus.BAD_REQUEST, returnPage);
        }
    }

    private static boolean validateLogin(String login) {
        return login != null && !login.isEmpty();
    }

    private static boolean validatePassword(String password) {
        return password != null &&
                password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,16}$");
    }
}
