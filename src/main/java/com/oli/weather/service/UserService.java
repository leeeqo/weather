package com.oli.weather.service;

import com.oli.weather.entity.Session;
import com.oli.weather.entity.User;
import com.oli.weather.exception.ApplicationException;
import com.oli.weather.repository.SessionRepository;
import com.oli.weather.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    public User getUserBySessionId(String sessionId) {
        System.out.println("SessionId = " + sessionId);
        System.out.println("Integer sessionID = " + Integer.parseInt(sessionId));

        Session session = sessionRepository.findById(Integer.parseInt(sessionId))
                .orElseThrow(() -> new ApplicationException("Session by id not found"));

        System.out.println("SessionId = " + session.getId());
        System.out.println("User is null? = " + (session.getUser() == null));

        return userRepository.findById(session.getUser().getId())
                .orElseThrow(() -> new ApplicationException("User for specified session not found"));
    }
}
