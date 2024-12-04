package com.oli.weather.service;

import com.oli.weather.entity.Session;
import com.oli.weather.entity.User;
import com.oli.weather.exception.user.AuthorizationException;
import com.oli.weather.exception.user.NotFoundException;
import com.oli.weather.exception.user.ResourceAlreadyExists;
import com.oli.weather.repository.SessionRepository;
import com.oli.weather.repository.UserRepository;
import com.password4j.Password;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private SessionRepository sessionRepository;

    @InjectMocks
    private AuthService authService;

    private final User user = User.builder()
            .login("TestLogin")
            .password("1234Test")
            .build();

    private final Session session = Session.builder()
            .user(user)
            .build();

    @Test
    public void testVerifyUser_SessionIsNotPresent() {
        User savedUser = User.builder()
                .id(1)
                .login(user.getLogin())
                .password(Password.hash(user.getPassword()).withBcrypt().getResult())
                .build();

        Session savedSession = Session.builder().id(1).user(session.getUser()).build();

        Optional<User> optionalUser = Optional.of(savedUser);
        Optional<Session> optionalSession = Optional.empty();

        when(userRepository.findByLogin(user.getLogin())).thenReturn(optionalUser);

        when(sessionRepository.findByUser(savedUser)).thenReturn(optionalSession);

        when(sessionRepository.save(argThat(session -> session.getUser().equals(savedUser))))
                .thenReturn(savedSession);

        int savedSessionId = authService.verifyUser(user);

        assertEquals(savedSession.getId(), savedSessionId);
    }

    @Test
    public void testRegisterUser_SessionIsActive() {
        User savedUser = User.builder()
                .id(1)
                .login(user.getLogin())
                .password(Password.hash(user.getPassword()).withBcrypt().getResult())
                .build();

        Session savedSession = Session.builder()
                .id(1)
                .user(session.getUser())
                .expiresAt(LocalDateTime.now().plusHours(1))
                .build();

        Optional<User> optionalUser = Optional.of(savedUser);
        Optional<Session> optionalSession = Optional.of(savedSession);

        when(userRepository.findByLogin(user.getLogin())).thenReturn(optionalUser);

        when(sessionRepository.findByUser(savedUser)).thenReturn(optionalSession);

        int savedSessionId = authService.verifyUser(user);

        assertEquals(savedSession.getId(), savedSessionId);
    }

    @Test
    public void testRegisterUser_SessionIsExpired() {
        User savedUser = User.builder()
                .id(1)
                .login(user.getLogin())
                .password(Password.hash(user.getPassword()).withBcrypt().getResult())
                .build();

        Session savedSession = Session.builder()
                .id(1)
                .user(session.getUser())
                .expiresAt(LocalDateTime.now().minusHours(1))
                .build();

        Session newSession = Session.builder()
                .id(2)
                .user(session.getUser())
                .expiresAt(LocalDateTime.now().plusDays(1))
                .build();

        Optional<User> optionalUser = Optional.of(savedUser);
        Optional<Session> optionalSession = Optional.of(savedSession);

        when(userRepository.findByLogin(user.getLogin())).thenReturn(optionalUser);

        when(sessionRepository.findByUser(savedUser)).thenReturn(optionalSession);

        doNothing().when(sessionRepository).delete(optionalSession.get());

        when(sessionRepository.save(argThat(session -> session.getUser().equals(savedUser))))
                .thenReturn(newSession);

        int savedSessionId = authService.verifyUser(user);

        assertEquals(newSession.getId(), savedSessionId);
    }

    @Test
    public void testRegisterUser_InvalidLogin() {
        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> authService.verifyUser(user));

        String expectedMessage = "User with login " + user.getLogin() + " not found.";

        assertEquals(expectedMessage, ex.getAdditionalMessage());
    }

    @Test
    public void testRegisterUser_InvalidPassword() {
        User savedUser = User.builder()
                .id(1)
                .login(user.getLogin())
                .password(Password.hash("Some1Other1Password").withBcrypt().getResult())
                .build();

        Optional<User> optionalUser = Optional.of(savedUser);

        when(userRepository.findByLogin(user.getLogin())).thenReturn(optionalUser);

        AuthorizationException ex = assertThrows(
                AuthorizationException.class,
                () -> authService.verifyUser(user));

        String expectedMessage = "Invalid password";

        assertEquals(expectedMessage, ex.getAdditionalMessage());
    }

    @Test
    public void testRegisterUser_ValidUser() {
        User savedUser = User.builder()
                .id(1)
                .login(user.getLogin())
                .password(Password.hash(user.getPassword()).withBcrypt().getResult())
                .build();

        Session savedSession = Session.builder()
                .id(1)
                .user(session.getUser())
                .build();

        when(userRepository.save(user)).thenReturn(savedUser);
        when(sessionRepository.save(argThat(session -> session.getUser().equals(savedUser))))
                .thenReturn(savedSession);

        int savedSessionId = authService.registerUser(user);

        assertEquals(savedSession.getId(), savedSessionId);
    }

    @Test
    public void testRegisterUser_LoginIsAlreadyInDB() {
        when(userRepository.save(user)).thenThrow(DataIntegrityViolationException.class);

        ResourceAlreadyExists ex = assertThrows(
                ResourceAlreadyExists.class,
                () -> authService.registerUser(user));

        String expectedMessage = "User with login " + user.getLogin() + " already exists.";

        assertEquals(expectedMessage, ex.getAdditionalMessage());
    }
}
