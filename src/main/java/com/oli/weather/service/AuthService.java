package com.oli.weather.service;

import com.oli.weather.entity.Session;
import com.oli.weather.entity.User;
import com.oli.weather.exception.ApplicationException;
import com.oli.weather.exception.user.AuthorizationException;
import com.oli.weather.exception.user.NotFoundException;
import com.oli.weather.exception.user.ResourceAlreadyExists;
import com.oli.weather.repository.SessionRepository;
import com.oli.weather.repository.UserRepository;
import com.password4j.Password;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class AuthService {

    private static final int LIFE_DAYS = 1;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    public Integer verifyUser(User user) {
        String login = user.getLogin();
        String password = user.getPassword();

        user = userRepository.findByLogin(login)
                .orElseThrow(() -> new NotFoundException("User with login " + login + " not found.", "/sign-in"));

        if (!Password.check(password, user.getPassword()).withBcrypt()) {
            log.debug("Wrong password for user " + login + ".");

            throw new AuthorizationException("Invalid password", HttpStatus.UNAUTHORIZED, "/sign-in");
        }

        Optional<Session> optionalSession = sessionRepository.findByUser(user);

        if (optionalSession.isPresent()) {
            if (optionalSession.get().getExpiresAt().isBefore(LocalDateTime.now())) {
                log.debug("Session for user " + user.getLogin() + " is expired.");
                log.debug("Deleting session for user " + user.getLogin());

                sessionRepository.delete(optionalSession.get());
            } else {
                log.debug("Session for user " + user.getLogin() + " is active. Returning this session.");

                return optionalSession.get().getId();
            }
        }

        log.debug("There is no session for user " + user.getLogin() + ". Creating new one.");

        Session session = Session.builder()
                .user(user)
                .expiresAt(LocalDateTime.now().plusDays(LIFE_DAYS))
                .build();

        Session saved = sessionRepository.save(session);

        return saved.getId();
    }

    public Integer registerUser(User user) {
        user.setPassword(Password.hash(user.getPassword()).withBcrypt().getResult());

        User savedUser = null;
        try {
            savedUser = userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceAlreadyExists(
                    "User with login " + user.getLogin() + " already exists.",
                    "/sign-up");
        }

        Session session = Session.builder()
                .user(savedUser)
                .expiresAt(LocalDateTime.now().plusDays(LIFE_DAYS))
                .build();

        Session savedSession = sessionRepository.save(session);

        return savedSession.getId();
    }

    public void removeSession(String sessionId) {
        Session session = sessionRepository.findById(Integer.parseInt(sessionId))
                .orElseThrow(() -> new ApplicationException("Session not found."));

        sessionRepository.delete(session);
    }
}
