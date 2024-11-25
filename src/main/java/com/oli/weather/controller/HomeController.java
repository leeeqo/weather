package com.oli.weather.controller;

import com.oli.weather.dto.WeatherDTO;
import com.oli.weather.entity.Location;
import com.oli.weather.entity.User;
import com.oli.weather.service.OpenWeatherService;
import com.oli.weather.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

import static com.oli.weather.utils.RequestUtils.getSessionCookie;

@Slf4j
@Controller
@RequestMapping("/home")
public class HomeController {

    // TODO - 1) Unique Locations 3) Exceptions

    @Autowired
    private UserService userService;

    @Autowired
    private OpenWeatherService openWeatherService;

    @GetMapping
    public String homePage(HttpServletRequest request, Model model) {
        log.debug("In HomeController");

        String sessionId = getSessionCookie(request);

        log.debug("SessionId = " + sessionId);

        User user = null;
        Map<Location, WeatherDTO> locationWeatherMap = null;

        if (sessionId != null) {
            user = userService.getUserBySessionId(sessionId);
            locationWeatherMap = openWeatherService.getLocationWeatherMap(user);
        }

        model.addAttribute("user", user);
        model.addAttribute("locationWeatherMap", locationWeatherMap);

        return "home";
    }
}
