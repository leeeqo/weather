package com.oli.weather.service;

import com.oli.weather.entity.Location;
import com.oli.weather.entity.Session;
import com.oli.weather.entity.User;
import com.oli.weather.exception.ApplicationException;
import com.oli.weather.repository.LocationRepository;
import com.oli.weather.repository.SessionRepository;
import com.oli.weather.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LocationService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    public void addLocation(String sessionId, Location location) {
        Session session = sessionRepository.findById(Integer.parseInt(sessionId))
                .orElseThrow(() -> new ApplicationException("Session by id not found"));

        log.debug("SessionId = " + sessionId);
        log.debug("User is null? = " + (session.getUser() == null));

        User user = userRepository.findById(session.getUser().getId())
                .orElseThrow(() -> new ApplicationException("User for specified session not found"));

        location.setUser(user);

        locationRepository.save(location);
    }
}
