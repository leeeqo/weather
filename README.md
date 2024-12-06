# Weather Project

![weather_home](https://github.com/user-attachments/assets/b4fd0b87-fc80-4aee-ad0e-5370ec04685b)

Web-application for weather tracking.
User can authorize and add some locations to track its weather.
User can also watch weather for specific location without authorization.

Tech task: https://zhukovsd.github.io/java-backend-learning-course/projects/weather-viewer/

### Stack:

- Spring (Boot, Data)
- Hibernate
- PostgreSQL
- Liquibase 
- JUnit, Mockito
- Thymeleaf
- Docker

### How to run:

- Open project in IDE.
- In root directory create .env file with env variables:
```
POSTGRES_USERNAME=your_username 
POSTGRES_PASSWORD=your_password
```
- Start PostgreSQL container via compose.yaml file.
- Start WeatherApplication with environment variables:
```
POSTGRES_USERNAME=your_username;POSTGRES_PASSWORD=your_password;POSTGRES_URL=jdbc:postgresql://your_host:5432/postgres-weather;OPEN_WEATHER_APIKEY=your_api_key
```
#### It's important to add these variables to Gradle Configuration Template too to be able to use gradle commands.

### Features:

#### User:

- Registration
- Authorization
- Logout

#### Main:

- Search for locations
- Weather by specific location
- Locations tracking (only for authorized)
- Getting locations list with weather (only for authorized)
- Deleting locations

### In progress:

- Other tests
- docker.compose - weather service
