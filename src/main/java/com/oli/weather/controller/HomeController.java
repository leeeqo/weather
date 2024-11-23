package com.oli.weather.controller;

import com.oli.weather.dto.LocationDTO;
import com.oli.weather.dto.WeatherDTO;
import com.oli.weather.entity.User;
import com.oli.weather.service.OpenWeatherService;
import com.oli.weather.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

import static com.oli.weather.utils.RequestUtils.getSessionCookie;

@Controller
public class HomeController {

    // TODO - 1) Unique Locations 2) Empty search result 3) Exceptions

    @Autowired
    private UserService userService;

    @Autowired
    private OpenWeatherService openWeatherService;

    @GetMapping("/home")
    public String index(HttpServletRequest request, Model model) {
        System.out.println("IN HOME CONTROLLER");

        String sessionId = getSessionCookie(request);

        System.out.println("Session Cookie? = " + sessionId);

        User user = null;
        Map<LocationDTO, WeatherDTO> locationWeatherMap = null;

        if (sessionId != null) {
            user = userService.getUserBySessionId(sessionId);
            locationWeatherMap = openWeatherService.getLocationWeatherMap(user);
        }

        model.addAttribute("user", user);
        model.addAttribute("locationWeatherMap", locationWeatherMap);

        return "home";
    }
}
