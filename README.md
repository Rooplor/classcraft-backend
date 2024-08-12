# classcraft-backend

## Overview
This is a Kotlin-based Spring Boot project. The project is built using Maven, and this README provides instructions to set up and run the project locally.

## Setup
1. Build the project:
Run the following command to build the project using Maven:
```bash
mvn clean install
```

2. Run the application: Use the following command to start the Spring Boot application:
```bash
mvn spring-boot:run
```

Alternatively, you can run the JAR file directly:
```bash
java -jar target/your-application.jar
```

## Configuration
The application properties can be configured in the src/main/resources/application.properties or src/main/resources/application.yml file.

## Running Tests
To run the tests, use the following Maven command:

```bash
mvn test
```

## Accessing the Application
Once the application is running, you can access it at:

```
http://localhost:8080
```
