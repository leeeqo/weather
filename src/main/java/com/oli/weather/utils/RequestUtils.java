package com.oli.weather.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Optional;

public class RequestUtils {

    private static final String SESSION_COOKIE_NAME = "sessionId";

    public static Optional<String> getSessionCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return Optional.empty();
        }

        Optional<Cookie> sessionId = Arrays.stream(cookies)
                .filter(cookie -> SESSION_COOKIE_NAME.equals(cookie.getName()))
                .findFirst();

        return sessionId.map(Cookie::getValue);
    }
}
