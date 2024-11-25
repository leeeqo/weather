package com.oli.weather.service;

import com.oli.weather.client.OpenWeatherClient;
import com.oli.weather.dto.LocationDTO;
import com.oli.weather.dto.WeatherDTO;
import com.oli.weather.entity.Location;
import com.oli.weather.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.oli.weather.utils.MathUtils.convertKelvinToCelsius;
import static com.oli.weather.utils.MathUtils.round;

@Service
public class OpenWeatherService {

    private static final int ROUNDING_PLACES = 1;

    @Autowired
    private OpenWeatherClient openWeatherClient;

    public List<LocationDTO> getLocationsByName(String locationName) {
        return openWeatherClient.findLocationsByName(locationName);
    }

    public Map<Location, WeatherDTO> getLocationWeatherMap(User user) {
        return user.getLocations().stream()
                .collect(
                        Collectors.toMap(Function.identity(), this::getWeatherForLocation)
                );
    }

    public WeatherDTO getWeatherForLocation(Location location) {
        WeatherDTO weatherDTO = openWeatherClient.findWeatherByLocation(location);

        weatherDTO.setTemperature(
                convertKelvinToCelsius(weatherDTO.getTemperature(), ROUNDING_PLACES));
        weatherDTO.setFeelsLike(
                convertKelvinToCelsius(weatherDTO.getFeelsLike(), ROUNDING_PLACES));

        weatherDTO.setDate(LocalDateTime.now());

        return weatherDTO;
    }
}
