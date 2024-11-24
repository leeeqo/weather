package com.oli.weather.service;

import com.oli.weather.entity.Session;
import com.oli.weather.entity.User;
import com.oli.weather.exception.ApplicationException;
import com.oli.weather.repository.SessionRepository;
import com.oli.weather.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    public User getUserBySessionId(String sessionId) {
        log.debug("SessionId = " + sessionId);

        Session session = sessionRepository.findById(Integer.parseInt(sessionId))
                .orElseThrow(() -> new ApplicationException("Session by id not found"));

        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            log.info("Attempt to use expired session. Returning null user.");

            return null;
        }

        log.debug("SessionId = " + session.getId());
        log.debug("User is null? = " + (session.getUser() == null));

        return userRepository.findById(session.getUser().getId())
                .orElseThrow(() -> new ApplicationException("User for specified session not found."));
    }
}
