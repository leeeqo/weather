package com.oli.weather.service;

import com.oli.weather.client.OpenWeatherClient;
import com.oli.weather.dto.LocationDTO;
import com.oli.weather.dto.UserDTO;
import com.oli.weather.dto.UserLocationsWeatherDTO;
import com.oli.weather.dto.WeatherDTO;
import com.oli.weather.entity.Location;
import com.oli.weather.entity.Session;
import com.oli.weather.entity.User;
import com.oli.weather.exception.ApplicationException;
import com.oli.weather.repository.SessionRepository;
import com.oli.weather.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HomeService {

    @Autowired
    private OpenWeatherClient openWeatherClient;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    public UserLocationsWeatherDTO getUserLocationsWeather(String sessionId) {
        // TODO - sessionId shouldn't be passed to cookies directly.
        // TODO - Change by hashed value

        System.out.println("SessionId = " + sessionId);
        System.out.println("Integer sessionID = " + Integer.parseInt(sessionId));

        Session session = sessionRepository.findById(Integer.parseInt(sessionId))
                .orElseThrow(() -> new ApplicationException("Session by id not found"));

        System.out.println("SessionId = " + session.getId());
        System.out.println("User is null? = " + (session.getUser() == null));

        User user = userRepository.findById(session.getUser().getId())
                .orElseThrow(() -> new ApplicationException("User for specified session not found"));

        Map<LocationDTO, WeatherDTO> locationDTOWeatherDTOMap = user.getLocations().stream()
                .collect(
                        Collectors.toMap(this::locationToLocationDTO, this::getWeatherForLocation)
                );

        return UserLocationsWeatherDTO.builder()
                .user(user)
                .locationWeatherMap(locationDTOWeatherDTOMap)
                .build();
    }

    private WeatherDTO getWeatherForLocation(Location location) {
        return openWeatherClient.findWeatherByLocation(location);
    }

    private LocationDTO locationToLocationDTO(Location location) {
        return LocationDTO.builder()
                .name(location.getName())
                .lat(location.getLatitude())
                .lon(location.getLongitude())
                .build();
    }
}
