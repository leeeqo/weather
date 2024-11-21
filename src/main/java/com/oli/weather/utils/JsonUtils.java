package com.oli.weather.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> T readJsonFromResponse(HttpResponse<String> response, TypeReference<T> valueType) throws IOException {
        T obj = null;
        try {
            obj = OBJECT_MAPPER.readValue(response.body(), valueType);
        } catch (DatabindException e) {

            // TODO
            throw new RuntimeException("Bad response");
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
}
