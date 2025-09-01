# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Cashly API is a Spring Boot 3.5.5 financial management API built with Clean Architecture principles, using Java 24, MySQL, and follows DDD/TDD methodologies.

## Development Commands

### Build and Run
```bash
# Run the application
./mvnw spring-boot:run

# Run with test configuration (uses Testcontainers)
./mvnw spring-boot:run -Dspring-boot.run.main-class=com.cashly.cashly_api.TestCashlyApplication

# Build the project
./mvnw clean package

# Skip tests during build
./mvnw clean package -DskipTests
```

### Testing
```bash
# Run all tests
./mvnw test

# Run unit tests specifically
./mvnw test -Dtest="**/*UnitTest"

# Run integration tests specifically
./mvnw test -Dtest="**/*IntegrationTest"

# Run E2E tests specifically
./mvnw test -Dtest="**/*E2ETest"

# Run tests with coverage report
./mvnw test jacoco:report
```

### Database
```bash
# Start MySQL via Docker Compose
docker-compose up -d mysql

# View database logs
docker-compose logs mysql

# Stop database
docker-compose down
```

## Architecture Structure

The project follows Clean Architecture with these layers:

### Domain Layer (`src/main/java/com/cashly/cashly_api/domain/`)
- `entities/` - Core business entities with identity
- `valueobjects/` - Immutable descriptive objects  
- `services/` - Domain business logic
- `events/` - Domain events for business events

### Application Layer (`src/main/java/com/cashly/cashly_api/application/`)
- `usecases/` - Orchestrate domain objects for business requirements
- `ports/` - Interfaces for external dependencies
- `dto/` - Data transfer objects

### Infrastructure Layer (`src/main/java/com/cashly/cashly_api/infrastructure/`)
- `persistence/` - Database implementations and repositories
- `web/` - REST controllers and request/response handling
- `security/` - Authentication and authorization
- `config/` - Application configurations

### Shared Layer (`src/main/java/com/cashly/cashly_api/shared/`)
- `exceptions/` - Common exceptions
- `utils/` - Utility classes
- `constants/` - Application constants

## Key Technologies

- **Framework**: Spring Boot 3.5.5 with Spring Security, Spring Data JPA
- **Database**: MySQL with Flyway migrations
- **Testing**: JUnit 5, Testcontainers, Mockito
- **Build**: Maven
- **Java**: Version 24

## Testing Strategy

- **Unit Tests**: 70% - Test individual components in isolation
- **Integration Tests**: 20% - Test component interactions
- **End-to-End Tests**: 10% - Test complete workflows
- Uses Testcontainers for integration testing with real MySQL instances
- Test naming follows `should_DoSomething_When_ConditionMet()` convention
- Tests use AAA pattern (Arrange, Act, Assert)

## Database Configuration

The application uses MySQL with the following setup:
- Docker Compose provides local MySQL instance
- Flyway handles database migrations in `src/main/resources/db/migration/`
- Testcontainers provides isolated test databases

## Environment Setup

The project includes:
- `TestCashlyApplication.java` - Test runner with Testcontainers integration
- `TestcontainersConfiguration.java` - MySQL container configuration for tests
- `compose.yaml` - Local development MySQL setup