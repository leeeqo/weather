package com.oli.weather.controller;

import com.oli.weather.dto.LocationDTO;
import com.oli.weather.service.OpenWeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {

    @Autowired
    private OpenWeatherService openWeatherService;

    @GetMapping("/search")
    public String getLocations(@RequestParam("locationName") String locationName, Model model) {
        List<LocationDTO> locationDTOList = openWeatherService.getLocationsByName(locationName);

        model.addAttribute("locations", locationDTOList);

        return "locations";
    }
}
