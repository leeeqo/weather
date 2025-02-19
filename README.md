# Weather Project

![weather_home](https://github.com/user-attachments/assets/b4fd0b87-fc80-4aee-ad0e-5370ec04685b)

Web-application for weather tracking.
User can authorize and add some locations to track its weather.
User can also watch weather for specific location without authorization.

Tech task: [here](https://zhukovsd.github.io/java-backend-learning-course/projects/weather-viewer/)

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
- Set your variables in .env file. To get API key you have to sign up on [openweathermap.org](https://openweathermap.org/):
```
POSTGRES_USERNAME=your_username
POSTGRES_PASSWORD=your_password
OPEN_WEATHER_APIKEY=your_key
```
- Start PostgreSQL container via docker-compose.yaml file.
- Start Weather container via docker-compose.yaml file.

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
