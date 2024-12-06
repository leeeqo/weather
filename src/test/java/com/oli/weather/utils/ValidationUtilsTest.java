package com.oli.weather.utils;

import com.oli.weather.entity.User;
import com.oli.weather.exception.user.AuthorizationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.oli.weather.utils.ValidationUtils.validateLoginAndPassword;
import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilsTest {

    private final String returnPageStub = "/stub";

    @ParameterizedTest
    @CsvSource({
            "login, 123456aA",
            "t, aaaaa12Abkc",
            "123, ABCDEFg123"
    })
    public void givenCorrectUser_whenValidate_thenDoNotThrowException(String login, String password) {
        User user = buildUser(login, password);

        assertDoesNotThrow(() -> validateLoginAndPassword(user, returnPageStub));
    }

    @ParameterizedTest
    @CsvSource({
            ", 123456aA",
            ", w"
    })
    public void givenInvalidLogin_whenValidate_thenThrowException(String login, String password) {
        User user = buildUser(login, password);

        AuthorizationException ex = assertThrows(
                AuthorizationException.class,
                () -> validateLoginAndPassword(user, returnPageStub));

        String expectedMessage = "Login must be provided";

        assertEquals(expectedMessage, ex.getAdditionalMessage());
        assertEquals(returnPageStub, ex.getReturnPage());
    }

    @ParameterizedTest
    @CsvSource({
            "login, 123",
            "login, w",
            "login, mmmmmmmmmmmmmm",
            "login, password123",
            "login, WEDTW124325",
            "login, 12325445",
            "login, 1234aaaaAAAAbbbbBBBB",
            "login, "
    })
    public void givenInvalidPassword_whenValidate_thenThrowException(String login, String password) {
        User user = buildUser(login, password);

        AuthorizationException ex = assertThrows(
                AuthorizationException.class,
                () -> validateLoginAndPassword(user, returnPageStub));

        String expectedMessage = "Password should contain:\n" +
                "1 number,\n" +
                "1 uppercase letter,\n" +
                "1 lowercase letter.\n" +
                "Password should be 8-16 characters with no space.";

        assertEquals(expectedMessage, ex.getAdditionalMessage());
        assertEquals(returnPageStub, ex.getReturnPage());
    }

    private User buildUser(String login, String password) {
        return User.builder()
                .login(login)
                .password(password)
                .build();
    }
}