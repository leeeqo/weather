package com.oli.weather.service;

import com.oli.weather.entity.Location;
import com.oli.weather.entity.Session;
import com.oli.weather.entity.User;
import com.oli.weather.exception.ApplicationException;
import com.oli.weather.exception.user.ResourceAlreadyExists;
import com.oli.weather.repository.LocationRepository;
import com.oli.weather.repository.SessionRepository;
import com.oli.weather.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @MockBean
    private SessionRepository sessionRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationService locationService;

    private final User user = User.builder()
            .login("TestLogin")
            .password("1234Test")
            .build();

    private final Session session = Session.builder()
            .user(user)
            .build();

    private final Location location = Location.builder()
            .name("Tallinn")
            .latitude(59.4370)
            .longitude(24.7536)
            .user(user)
            .build();

    @Test
    public void testAddLocation_NoSessionId() {
        String sessionId = "1";

        Optional<Session> optionalSession = Optional.empty();

        when(sessionRepository.findById(Integer.parseInt(sessionId))).thenReturn(optionalSession);

        ApplicationException ex = assertThrows(
                ApplicationException.class,
                () -> locationService.addLocation(sessionId, location));

        String expectedMessage = "Session by id was not found";

        assertEquals(expectedMessage, ex.getAdditionalMessage());
    }

    @Test
    public void testAddLocation_NoUserForSessionId() {
        String sessionId = "1";

        Optional<Session> optionalSession = Optional.of(session);
        Optional<User> optionalUser = Optional.empty();

        when(sessionRepository.findById(Integer.parseInt(sessionId))).thenReturn(optionalSession);

        when(userRepository.findById(session.getUser().getId())).thenReturn(optionalUser);

        ApplicationException ex = assertThrows(
                ApplicationException.class,
                () -> locationService.addLocation(sessionId, location));

        String expectedMessage = "User for specified session was not found";

        assertEquals(expectedMessage, ex.getAdditionalMessage());
    }

    @Test
    public void testAddLocation_LocationIsAlreadyTracked() {
        String sessionId = "1";

        Optional<Session> optionalSession = Optional.of(session);
        Optional<User> optionalUser = Optional.of(user);

        when(sessionRepository.findById(Integer.parseInt(sessionId))).thenReturn(optionalSession);

        when(userRepository.findById(session.getUser().getId())).thenReturn(optionalUser);

        when(locationRepository.save(location)).thenThrow(DataIntegrityViolationException.class);

        ResourceAlreadyExists ex = assertThrows(
                ResourceAlreadyExists.class,
                () -> locationService.addLocation(sessionId, location));

        String expectedMessage = "Location is already tracked.";

        assertEquals(expectedMessage, ex.getAdditionalMessage());
    }

    @Test
    public void testAddLocation_Ok() {
        String sessionId = "1";

        Optional<Session> optionalSession = Optional.of(session);
        Optional<User> optionalUser = Optional.of(user);

        when(sessionRepository.findById(Integer.parseInt(sessionId))).thenReturn(optionalSession);

        when(userRepository.findById(session.getUser().getId())).thenReturn(optionalUser);

        assertDoesNotThrow(() -> locationRepository.save(location));
    }
}