# Insurance Management Platform

## Project Description

The Insurance Management Platform is a Spring Boot application built to manage clients, policies, and claims
efficiently. It allows users to perform CRUD operations on these entities while ensuring data integrity, validation, and
a seamless API experience. The application uses Spring Boot 3, Java 17, and follows modern Spring practices with
features like Java Records for DTOs, global exception handling, and input validation.

## Features

- **Client Management**: Add, update, view, and delete client information.
- **Policy Management**: Manage insurance policies, including coverage amounts, policy types, and premium details.
- **Claims Management**: File and track insurance claims with status updates.
- **Input Validation**: Automatic validation of user inputs using Spring's validation annotations.
- **Exception Handling**: Centralized and consistent exception handling across the application.
- **RESTful APIs**: Well-structured and documented REST APIs for interaction with clients, policies, and claims.

## Project Structure

The project follows a standard Spring Boot structure:

```
insurance-management/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── dev/sagar/insurance/
│   │   │       ├── config/            # Configuration classes (e.g., Spring beans, properties)
│   │   │       ├── controller/        # REST Controllers for handling HTTP requests
│   │   │       ├── dto/               # Data Transfer Objects (DTOs) using Java Records
│   │   │       ├── entity/            # JPA Entities representing database models
│   │   │       ├── exception/         # Custom exceptions and global exception handling
│   │   │       ├── mapper/            # Mappers for converting between entities and DTOs
│   │   │       ├── repository/        # Spring Data JPA Repositories for data access
│   │   │       ├── service/           # Business logic and service classes
│   │   │       └── InsuranceManagementApplication.java  # Main application entry point
│   │   └── resources/
│   │       └── application.yaml        # Application configuration
│   └── test/
│       └── java/
│           └── dev/sagar/insurance/
│               ├── controller/
│               ├── service/
│               └── mapper/
├── pom.xml                            # Maven build file
├── README.md                          # Project documentation
├── Dockerfile                         # Docker build file
└── LICENSE                            # Project license
```

### Key Packages

- **config**: Configuration classes (e.g., Spring beans, properties).
- **controller**: Contains REST controllers that expose APIs for clients, policies, and claims.
- **dto**: Holds Java Records used as DTOs for request and response objects.
- **entity**: JPA entities that represent database tables.
- **exception**: Global exception handling and custom exceptions.
- **mapper**: Converts entities to DTOs and vice versa.
- **repository**: JPA repositories for interacting with the database.
- **service**: Business logic for clients, policies, and claims.

## Prerequisites

- Java 17
- Maven 3.8+
- Docker (optional, for running the application with Docker)

## Running the Application Locally

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/nathsagar96/insurance-management.git
   cd insurance-management
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
      docker build -t insurance-management .
      ```

    - Run the Docker container:

      ```bash
      docker run -p 8080:8080 insurance-management
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