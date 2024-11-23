package com.oli.weather.service;

import com.oli.weather.client.OpenWeatherClient;
import com.oli.weather.dto.LocationDTO;
import com.oli.weather.dto.UserLocationsWeatherDTO;
import com.oli.weather.dto.WeatherDTO;
import com.oli.weather.entity.Location;
import com.oli.weather.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OpenWeatherService {

    @Autowired
    private OpenWeatherClient openWeatherClient;

    public List<LocationDTO> getLocationsByName(String locationName) {
        return openWeatherClient.findLocationsByName(locationName);
    }

    public Map<LocationDTO, WeatherDTO> getLocationWeatherMap(User user) {
        return user.getLocations().stream()
                .collect(
                        Collectors.toMap(this::locationToLocationDTO, this::getWeatherForLocation)
                );
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
