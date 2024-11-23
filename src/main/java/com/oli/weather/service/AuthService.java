package com.oli.weather.service;

import com.oli.weather.dto.UserDTO;
import com.oli.weather.entity.Session;
import com.oli.weather.entity.User;
import com.oli.weather.exception.ApplicationException;
import com.oli.weather.repository.SessionRepository;
import com.oli.weather.repository.UserRepository;
import com.password4j.Password;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Integer verifyUser(UserDTO userDTO) {
        String login = userDTO.getLogin();
        String password = userDTO.getPassword();

        Optional<User> optional = userRepository.findByLogin(login);

        if (optional.isPresent()) {
            User user = optional.get();

            String hashedDB = user.getPassword();

            boolean verified = Password.check(password, hashedDB).withBcrypt();

            log.debug("User " + user.getLogin() + " verified? = " + verified);

            if (!verified) {
                // TODO
                log.debug("Throwing exception");

                throw new ApplicationException();
            }

            Optional<Session> optionalSession = sessionRepository.findByUser(user);

            // TODO
            if (optionalSession.isPresent()) {
                if (optionalSession.get().getExpiresAt().isAfter(LocalDateTime.now())) {
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
        } else {
            log.debug("Throwing exception");

            throw new ApplicationException();
        }
    }

    public Integer registerUser(UserDTO userDTO) {
        String login = userDTO.getLogin();
        String password = userDTO.getPassword();

        Optional<User> optional = userRepository.findByLogin(login);

        if (optional.isPresent()) {
            // TODO
            throw new ApplicationException("User with login " + login + " already exists");
        }

        // TODO Validation of password

        User user = User.builder()
                .login(login)
                .password(Password.hash(password).withBcrypt().getResult())
                .build();

        User savedUser = userRepository.save(user);

        Session session = Session.builder()
                .user(savedUser)
                .expiresAt(LocalDateTime.now().plusDays(LIFE_DAYS))
                .build();

        Session savedSession = sessionRepository.save(session);

        return savedSession.getId();
    }

    public void removeSession(String sessionId) {
        Session session = sessionRepository.findById(Integer.parseInt(sessionId))
                .orElseThrow(() -> new ApplicationException("Session not found"));

        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            // TODO
            throw new ApplicationException("Session is expired.");
        }

        sessionRepository.delete(session);
    }
}
