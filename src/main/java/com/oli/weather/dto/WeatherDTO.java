package com.oli.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDTO {

    private Double lon;
    private Double lat;

    private String main;
    private String description;
    private String icon;

    private Double temperature;
    private Double feelsLike;
    private Double pressure;

    private Double windSpeed;

    private LocalDateTime date;
}
