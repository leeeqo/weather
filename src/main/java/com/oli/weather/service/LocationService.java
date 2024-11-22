package com.oli.weather.service;

import com.oli.weather.entity.Location;
import com.oli.weather.entity.Session;
import com.oli.weather.entity.User;
import com.oli.weather.exception.ApplicationException;
import com.oli.weather.repository.LocationRepository;
import com.oli.weather.repository.SessionRepository;
import com.oli.weather.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    public void addLocation(Integer sessionId, Location location) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ApplicationException("Session by id not found"));

        System.out.println("SessionId = " + session.getId());
        System.out.println("User is null? = " + (session.getUser() == null));

        User user = userRepository.findById(session.getUser().getId())
                .orElseThrow(() -> new ApplicationException("User for specified session not found"));

        location.setUser(user);

        locationRepository.save(location);
    }
}
