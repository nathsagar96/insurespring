# Insure Spring

## Project Description

The Insure Spring is a Spring Boot application designed for efficiently managing clients, policies, and
claims. It allows users to perform CRUD operations on these entities while ensuring data integrity, validation, and a
seamless API experience. The application uses Spring Boot 3, Java 21, and modern Spring practices, including features
like Java Records for DTOs, global exception handling, and input validation.

## Features

- **Client Management**: Add, update, view, and delete client information.
- **Policy Management**: Manage insurance policies, including coverage amounts, policy types, and premium details.
- **Claims Management**: File and track insurance claims with status updates.
- **Input Validation**: Automatic validation of user inputs using Spring's validation annotations.
- **Exception Handling**: Centralized and consistent exception handling across the application.
- **RESTful APIs**: Well-structured and documented REST APIs for interaction with clients, policies, and claims.

## Project Structure

The project follows a package-by-feature structure:

```
insure-spring/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── dev/sagar/insurance/
│   │   │       ├── claim/                # Logic and structure related to Claims
│   │   │       ├── client/               # Logic and structure related to Clients
│   │   │       ├── policy/               # Logic and structure related to Policies
│   │   │       ├── config/               # Configuration classes (e.g., Spring beans, properties)
│   │   │       ├── exception/            # Global exception handling and custom exceptions
│   │   │       └── InsureSpringApplication.java  # Main application entry point
│   │   └── resources/
│   │       └── application.yaml          # Application configuration
│   └── test/
│       └── java/
│           └── dev/sagar/insurance/
│               ├── claim/
│               ├── client/
│               └── policy/
├── pom.xml                               # Maven build file
├── README.md                             # Project documentation
├── Dockerfile                            # Docker build file
└── LICENSE                               # Project license
```

### Key Packages

- **claim, client, policy**: Each package contains the logic and related structures.
- **config**: Contains configuration classes (e.g., Spring beans, properties).
- **exception**: Handles global exceptions and contains custom exceptions.

## Prerequisites

- Java 21
- Maven 3.8+
- Docker (optional, for running the application with Docker)

## Running the Application Locally

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/nathsagar96/insure-spring.git
   cd insure-spring
   ```

2. **Build the Application:**

   Use Maven to build the application:

   ```bash
   mvn clean install
   ```

3. **Run the Application:**

   You can run the Spring Boot application using:

   ```bash
   mvn spring-boot:run
   ```

   The application will start and be accessible at `http://localhost:8080`.

4. **Running with Docker (Optional):**

   If you have Docker installed, you can run the application using Docker:

    - Build the Docker image:

      ```bash
      docker build -t insure-spring .
      ```

    - Run the Docker container:

      ```bash
      docker run -p 8080:8080 insure-spring
      ```

## API Documentation

Once the application is running, you can access the API documentation using:

```
http://localhost:8080/swagger-ui/index.html
```

This documentation provides details of all the available endpoints, their request and response structures, and sample
data.

## How to Contribute

If you want to contribute to this project, feel free to submit a pull request. For major changes, please open an issue
first to discuss what you would like to change.