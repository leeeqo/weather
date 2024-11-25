package com.oli.weather.controller;

import com.oli.weather.dto.LocationDTO;
import com.oli.weather.dto.WeatherDTO;
import com.oli.weather.entity.Location;
import com.oli.weather.entity.User;
import com.oli.weather.exception.ApplicationException;
import com.oli.weather.service.LocationService;
import com.oli.weather.service.OpenWeatherService;
import com.oli.weather.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.oli.weather.utils.RequestUtils.getSessionCookie;

@Controller
@RequestMapping("/location")
public class LocationController {

    private static final String REDIRECT_HOME = "/weather/home";
    private static final String WEATHER_BY_LOCATION = "?name=%s&lat=%s&lon=%s";

    @Autowired
    private OpenWeatherService openWeatherService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private UserService userService;

    @GetMapping("/search")
    public String getLocations(@RequestParam("locationName") String locationName,
                               HttpServletRequest request,
                               Model model) {

        String sessionId = getSessionCookie(request);

        User user = null;

        if (sessionId != null) {
            user = userService.getUserBySessionId(sessionId);
        }

        List<LocationDTO> locationDTOList = openWeatherService.getLocationsByName(locationName);

        model.addAttribute("user", user);
        model.addAttribute("locations", locationDTOList);

        return "locations";
    }

    @PostMapping("/track")
    public void trackLocation(Location location,
                              HttpServletRequest request,
                              HttpServletResponse response) throws IOException {

        String sessionId = getSessionCookie(request);

        if (sessionId == null) {
            throw new ApplicationException("Not authorized. Impossible to track location");
        }

        locationService.addLocation(sessionId, location);

        response.sendRedirect(REDIRECT_HOME);
    }

    @PostMapping
    public String locationWeather(Location location,
                                Model model) throws IOException {

        WeatherDTO weatherDTO = openWeatherService.getWeatherForLocation(location);

        model.addAttribute("location", location);
        model.addAttribute("weather", weatherDTO);

        return "weather-by-location";
    }

    @PostMapping("/delete")
    public void deleteLocation(String locationId,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException {

        String sessionId = getSessionCookie(request);

        if (sessionId == null) {
            // TODO
            throw new ApplicationException("Not authorized. Impossible to delete location");
        }

        locationService.deleteLocation(locationId);

        response.sendRedirect(REDIRECT_HOME);
    }
}
