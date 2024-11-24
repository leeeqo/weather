package com.oli.weather.utils;

import com.oli.weather.dto.UserDTO;
import com.oli.weather.entity.User;
import com.oli.weather.exception.user.AuthorizationException;
import com.oli.weather.exception.user.InvalidDataException;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {

    public static void validateLoginAndPassword(User user, String returnPage) {
        if (user.getLogin() == null || user.getLogin().isEmpty()) {
            throw new AuthorizationException("Login must be provided", HttpStatus.BAD_REQUEST, returnPage);
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new AuthorizationException("Password must be provided", HttpStatus.BAD_REQUEST, returnPage);
        }

        //String regEx = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,16}$";
        //Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        //Matcher matcher = pattern.matcher(user.getPassword());

        if (!user.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,16}$")) {
        //if (!matcher.matches()) {
            throw new AuthorizationException("Password should contain:\n" +
                     "1 number,\n" +
                     "1 uppercase letter,\n" +
                     "1 lowercase letter.\n" +
                     "Password should be 8-16 characters with no space.", HttpStatus.BAD_REQUEST, returnPage);
        }
    }
}
