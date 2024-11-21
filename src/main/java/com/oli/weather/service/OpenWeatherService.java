package com.oli.weather.service;

import com.oli.weather.client.OpenWeatherClient;
import com.oli.weather.dto.LocationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenWeatherService {

    @Autowired
    private OpenWeatherClient openWeatherClient;

    public List<LocationDTO> getLocationsByName(String locationName) {

        return openWeatherClient.findLocationsByName(locationName);
    }
}
