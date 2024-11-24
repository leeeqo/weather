package com.oli.weather.service;

import com.oli.weather.dto.UserDTO;
import com.oli.weather.entity.Session;
import com.oli.weather.entity.User;
import com.oli.weather.exception.ApplicationException;
import com.oli.weather.exception.user.AuthorizationException;
import com.oli.weather.exception.user.InvalidDataException;
import com.oli.weather.exception.user.NotFoundException;
import com.oli.weather.exception.user.ResourceAlreadyExists;
import com.oli.weather.repository.SessionRepository;
import com.oli.weather.repository.UserRepository;
import com.password4j.Password;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
                .orElseThrow(() -> new NotFoundException("User with login + " + login + " not found.", "/sign-in"));

        if (!Password.check(password, user.getPassword()).withBcrypt()) {
            log.debug("Invalid password for user " + login + ".");

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
        /*String login = userDTO.getLogin();
        String password = userDTO.getPassword();

        Optional<User> optional = userRepository.findByLogin(login);

        if (optional.isPresent()) {
            throw new ResourceAlreadyExists("User with login " + login + " already exists.");
        }

        User user = User.builder()
                .login(login)
                .password(Password.hash(password).withBcrypt().getResult())
                .build();*/

        Optional<User> optional = userRepository.findByLogin(user.getLogin());
        if (optional.isPresent()) {
            throw new ResourceAlreadyExists("...");
        }

        user.setPassword(Password.hash(user.getPassword()).withBcrypt().getResult());

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
