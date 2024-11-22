package com.oli.weather.dto;

import com.oli.weather.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLocationsWeatherDTO {

    private User user;
    private Map<LocationDTO, WeatherDTO> locationWeatherMap;
}
