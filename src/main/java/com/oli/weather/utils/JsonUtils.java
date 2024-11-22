package com.oli.weather.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.oli.weather.dto.WeatherDTO;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(WeatherDTO.class, new WeatherDeserializer());
        OBJECT_MAPPER.registerModule(simpleModule);
    }

    public static <T> T readJsonFromResponse(HttpResponse<String> response, Class<T> valueType) {
        T obj = null;
        try {
            System.out.println("IN READ JSON:");
            System.out.println("Body = " + response.body());

            obj = OBJECT_MAPPER.readValue(response.body(), valueType);
        } catch (JsonProcessingException e) {

            // TODO
            throw new RuntimeException(e.getMessage());
            //throw new IncorrectParameterException("Incorrect input data.");
        }

        return obj;
    }

    public static <T> List<T> readJsonListFromResponse(HttpResponse<String> response, Class<T> valueType) {
        List<T> lst = null;
        try {
            lst = OBJECT_MAPPER.readValue(response.body(), new TypeReference<List<T>>() {});
        } catch (JsonProcessingException e) {
            // TODO
            throw new RuntimeException(e);
        }

        return lst;
    }

    private static class WeatherDeserializer extends StdDeserializer<WeatherDTO> {

        public WeatherDeserializer() {
            this(null);
        }

        public WeatherDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public WeatherDTO deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
            JsonNode weatherNode = jp.getCodec().readTree(jp);

            WeatherDTO weatherDTO = new WeatherDTO();

            weatherDTO.setLon(weatherNode.get("coord").get("lon").asDouble());
            weatherDTO.setLat(weatherNode.get("coord").get("lat").asDouble());

            weatherDTO.setMain(weatherNode.get("weather").get(0).get("main").asText());
            weatherDTO.setDescription(weatherNode.get("weather").get(0).get("description").asText());
            weatherDTO.setIcon(weatherNode.get("weather").get(0).get("icon").asText());

            weatherDTO.setTemperature(weatherNode.get("main").get("temp").asDouble());
            weatherDTO.setFeelsLike(weatherNode.get("main").get("feels_like").asDouble());
            weatherDTO.setPressure(weatherNode.get("main").get("pressure").asDouble());

            weatherDTO.setWindSpeed(weatherNode.get("wind").get("speed").asDouble());

            return weatherDTO;
        }
    }
}
