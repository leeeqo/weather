package com.oli.weather.utils;

import com.oli.weather.dto.UserDTO;

public class ValidationUtils {

    public static void validateLoginAndPassword(UserDTO user) {
        if (user.getLogin() == null || user.getLogin().isEmpty()) {
            throw new RuntimeException();
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new RuntimeException();
        }
    }
}
