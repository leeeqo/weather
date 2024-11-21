package com.oli.weather.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.oli.weather.dto.LocationDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static com.oli.weather.utils.JsonUtils.readJsonListFromResponse;

@Component
public class OpenWeatherClient {

    private static final String API = "http://api.openweathermap.org";

    private static final String LOCATIONS = "/geo/1.0/direct?q=%s&limit=%s&appid=%s";
    private static final String WEATHER = "/data/2.5/weather?lat=%s&lon=%s&appid=%s";
    private static final String LIMIT = "6";

    @Value("${openWeather.APIKey}")
    private String APIKey;

    private final HttpClient client;

    public OpenWeatherClient() {
        this.client = HttpClient.newHttpClient();
    }

    public List<LocationDTO> findLocationsByName(String locationName) {
        String url = API + LOCATIONS.formatted(locationName, LIMIT, APIKey);

        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder(new URI(url)).GET().build();
        } catch (URISyntaxException e) {
            //TODO
            throw new RuntimeException(e);
        }

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            // TODO
            throw new RuntimeException(e);
        }

        return readJsonListFromResponse(response, LocationDTO.class);
    }
}
