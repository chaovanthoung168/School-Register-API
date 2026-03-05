# School Registration Application

This repository contains a Spring Boot based school registration system. It supports user authentication, department management, student registration, and payment processing.

## Features

- JWT-based authentication and authorization
- Department management with CRUD operations
- Student registration workflows
- Payment processing and transaction management
- Initial data population for development
- Swagger UI for REST API documentation

## Tech Stack

The application is built using the following technologies:

- **Java 17**
- **Spring Boot** (Web, Data JPA, Security, WebClient)
- **Gradle** build system (wrapper included)
- **H2** in-memory database for development (configurable to other RDBMS)
- **JWT** for stateless authentication
- **Swagger / Springdoc OpenAPI** for API documentation
- **MapStruct** (if mapper usage present) or custom mappers
- **Docker / Docker Compose** (optional) for containerized database
- **JUnit 5** and **Spring Test** for automated testing
- **Lombok** (if used in code)

## Getting Started

### Prerequisites

- Java 17 or newer
- Gradle 7.x (wrapper included)
- Docker and Docker Compose (optional for database)

### Running the Application

1. Clone the repository:

   ```bash
   git clone <repository-url>
   cd school-registration
   ```

2. Build and run using Gradle:

   ```bash
   ./gradlew bootRun
   ```

   Alternatively, build a jar and execute:

   ```bash
   ./gradlew clean build
   java -jar build/libs/school-registration-0.0.1-SNAPSHOT.jar
   ```

3. Configure application properties if needed by editing `src/main/resources/application.yaml`.

4. Access the application at `http://localhost:8080` and Swagger UI at `http://localhost:8080/swagger-ui.html`.

### Database

By default, the project configures an in-memory H2 database for development. To use another database, update `application.yaml` with the appropriate datasource settings.

### Tests

Run tests with:

```bash
./gradlew test
```

## Project Structure

See `src/main/java/co/thoung/schoolregistration` for the main packages:

- `config` - Spring configuration classes
- `domain` - JPA entity definitions
- `feature` - business logic by domain areas
- `security` - authentication/authorization
- `mapper` - DTO mapping
- `init` - data initialization
- `util` - utility classes

## External Integrations

The application supports connection to external services for payment and verification:

### Bakong API

Bakong is the national digital payment platform. This project integrates with Bakong to perform financial transactions, including generating payment QR codes.

- **Configuration**: add endpoint URLs and credentials to `application.yaml` under a `bakong` section.
- **Service layer**: a `BakongService` handles HTTP calls, token management, and request signing according to Bakong’s documentation.
- **QR code generation**: when a payment request is created, the service constructs the payload, sends it to Bakong, and retrieves a payment URI or a pre-signed QR code string.
- The QR code image is generated on-the-fly using libraries like `zxing` and returned to the client or embedded in HTML.

Sample configuration snippet:

```yaml
bakong:
  base-url: https://api.bakong.example
  client-id: your-client-id
  client-secret: your-client-secret
  merchant-id: your-merchant-id
```

Users can then trigger a payment via REST endpoint, which returns a QR code that customers scan with the Bakong mobile app to complete payment.

### Additional Notes

- Ensure proper error handling and retry logic around Bakong interactions.
- The integration is optional; if Bakong settings are missing, the related endpoints respond with a 503 or similar.
