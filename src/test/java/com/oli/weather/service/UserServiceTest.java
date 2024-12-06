package com.oli.weather.service;

import com.oli.weather.entity.Session;
import com.oli.weather.entity.User;
import com.oli.weather.exception.ApplicationException;
import com.oli.weather.repository.SessionRepository;
import com.oli.weather.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private SessionRepository sessionRepository;

    @InjectMocks
    private UserService userService;

    private final User user = User.builder()
            .id(1)
            .login("TestLogin")
            .password("1234Test")
            .build();

    private final Session session = Session.builder()
            .user(user)
            .build();

    @Test
    public void givenNoSavedSession_whenGetUserBySessionId_thenThrowException() {
        String sessionId = "1";

        Optional<Session> optionalSession = Optional.empty();

        when(sessionRepository.findById(Integer.parseInt(sessionId))).thenReturn(optionalSession);

        ApplicationException ex = assertThrows(
                ApplicationException.class,
                () -> userService.getUserBySessionId(sessionId));

        String expectedMessage = "Session by id not found";

        assertEquals(expectedMessage, ex.getAdditionalMessage());
    }

    @Test
    public void givenExpiredSession_whenGetUserBySessionId_thenReturnNull() {
        String sessionId = "1";

        session.setExpiresAt(LocalDateTime.now().minusHours(1));

        Optional<Session> optionalSession = Optional.of(session);

        when(sessionRepository.findById(Integer.parseInt(sessionId))).thenReturn(optionalSession);

        assertNull(userService.getUserBySessionId(sessionId));
    }

    @Test
    public void givenNoSavedUser_whenGetUserBySessionId_thenThrowException() {
        String sessionId = "1";

        session.setExpiresAt(LocalDateTime.now().plusHours(1));

        Optional<Session> optionalSession = Optional.of(session);
        Optional<User> optionalUser = Optional.empty();

        when(sessionRepository.findById(Integer.parseInt(sessionId))).thenReturn(optionalSession);

        when(userRepository.findById(session.getUser().getId())).thenReturn(optionalUser);

        ApplicationException ex = assertThrows(
                ApplicationException.class,
                () -> userService.getUserBySessionId(sessionId));

        String expectedMessage = "User for specified session not found.";

        assertEquals(expectedMessage, ex.getAdditionalMessage());
    }

    @Test
    public void givenValidSessionId_whenGetUserBySessionId_thenReturnUser() {
        String sessionId = "1";

        session.setExpiresAt(LocalDateTime.now().plusHours(1));

        Optional<Session> optionalSession = Optional.of(session);
        Optional<User> optionalUser = Optional.of(user);

        when(sessionRepository.findById(Integer.parseInt(sessionId))).thenReturn(optionalSession);

        when(userRepository.findById(session.getUser().getId())).thenReturn(optionalUser);

        User resultUser = userService.getUserBySessionId(sessionId);

        assertEquals(user, resultUser);
    }
}