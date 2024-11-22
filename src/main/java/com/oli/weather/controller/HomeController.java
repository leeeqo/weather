package com.oli.weather.controller;

import com.oli.weather.dto.UserLocationsWeatherDTO;
import com.oli.weather.service.HomeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

import static com.oli.weather.utils.RequestUtils.getSessionCookie;

@Controller
public class HomeController {

    @Autowired
    private HomeService homeService;

    @GetMapping("/home")
    public String index(HttpServletRequest request, Model model) {
        System.out.println("IN HOME CONTROLLER");

        Optional<String> optionalSessionId = getSessionCookie(request);

        System.out.println("Session Cookie? = " + optionalSessionId.isPresent());

        UserLocationsWeatherDTO userLocationsWeatherDTO = null;
        if (optionalSessionId.isPresent()) {
            String sessionId = optionalSessionId.get();

            // TODO
            if (!sessionId.isEmpty()) {
                userLocationsWeatherDTO = homeService.getUserLocationsWeather(sessionId);

                System.out.println("HOME CONTROLLER: USER = " + userLocationsWeatherDTO.getUser());
            }
        }

        model.addAttribute("userLocationsWeather", userLocationsWeatherDTO);

        return "home";
    }
}
