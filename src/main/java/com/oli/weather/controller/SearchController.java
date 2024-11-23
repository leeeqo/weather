package com.oli.weather.controller;

import com.oli.weather.dto.LocationDTO;
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

import static com.oli.weather.utils.RequestUtils.getSessionCookie;

@Controller
@RequestMapping("/search")
public class SearchController {

    private static final String REDIRECT_HOME = "/weather/home";

    @Autowired
    private OpenWeatherService openWeatherService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private UserService userService;

    @GetMapping
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

    @PostMapping
    public void trackLocation(Location location,
                              HttpServletRequest request,
                              HttpServletResponse response) throws IOException {

        String sessionId = getSessionCookie(request);

        if (sessionId == null) {
            // TODO
            throw new ApplicationException("Not authorized. Impossible to sign out");
        }

        locationService.addLocation(sessionId, location);

        response.sendRedirect(REDIRECT_HOME);
    }
}
