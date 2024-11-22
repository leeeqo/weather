package com.oli.weather.controller;

import com.oli.weather.dto.LocationDTO;
import com.oli.weather.entity.Location;
import com.oli.weather.exception.ApplicationException;
import com.oli.weather.service.LocationService;
import com.oli.weather.service.OpenWeatherService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.oli.weather.utils.RequestUtils.getSessionCookie;

@Controller
@RequestMapping("/search")
public class SearchController {

    private static final String REDIRECT_HOME = "/weather/home";

    @Autowired
    private OpenWeatherService openWeatherService;

    @Autowired
    private LocationService locationService;

    @GetMapping
    public String getLocations(@RequestParam("locationName") String locationName, Model model) {
        List<LocationDTO> locationDTOList = openWeatherService.getLocationsByName(locationName);

        model.addAttribute("locations", locationDTOList);

        return "locations";
    }

    @PostMapping
    public void trackLocation(Location location,
                              HttpServletRequest request,
                              HttpServletResponse response) throws IOException {

        Optional<String> optionalSessionId = getSessionCookie(request);

        if (optionalSessionId.isEmpty()) {
            // TODO
            throw new ApplicationException("Not authorized. Impossible to sign out");
        }

        Integer sessionId = Integer.parseInt(optionalSessionId.get());

        locationService.addLocation(sessionId, location);

        response.sendRedirect(REDIRECT_HOME);
    }
}
