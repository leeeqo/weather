FROM gradle:8.11.1-jdk17
COPY build/libs/weather-1.0.jar weather.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "weather.jar"]
