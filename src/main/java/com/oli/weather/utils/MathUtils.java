package com.oli.weather.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtils {

    private static final double ABSOLUTE_ZERO = -273.15;

    public static double convertKelvinToCelsius(double kelvinValue, int places) {
        return round(ABSOLUTE_ZERO + kelvinValue, places);
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }
}
