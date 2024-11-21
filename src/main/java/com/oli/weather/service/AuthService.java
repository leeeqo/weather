package com.oli.weather.service;

import com.oli.weather.dto.UserDTO;
import com.oli.weather.entity.Session;
import com.oli.weather.entity.User;
import com.oli.weather.exception.ApplicationException;
import com.oli.weather.repository.SessionRepository;
import com.oli.weather.repository.UserRepository;
import com.password4j.Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    private static final int LIFE_DAYS = 1;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    public Integer getSessionId(UserDTO userDTO) {
        String login = userDTO.getLogin();
        String password = userDTO.getPassword();

        Optional<User> optional = userRepository.findByLogin(login);

        if (optional.isPresent()) {
            User user = optional.get();

            String hashedDB = user.getPassword();

            boolean verified = Password.check(password, hashedDB).withBcrypt();

            System.out.println("Verffied? = " + verified);

            if (!verified) {
                // TODO
                System.out.println("Throwing exception");

                throw new ApplicationException();
            }

            Optional<Session> optionalSession = sessionRepository.findByUser(user);

            if (optionalSession.isPresent() && optionalSession.get().getExpiresAt().isAfter(LocalDateTime.now())) {
                System.out.println("Session is already in DB");

                return optionalSession.get().getId();
            } else {
                System.out.println("Creating new session!");

                Session session = Session.builder()
                        .user(user)
                        .expiresAt(LocalDateTime.now().plusDays(LIFE_DAYS))
                        .build();

                Session saved = sessionRepository.save(session);

                return saved.getId();
            }
        } else {
            System.out.println("Throwing exception");

            throw new ApplicationException();
        }
    }

    public Integer getSignUpSessionId(UserDTO userDTO) {
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
}
