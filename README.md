# Cashly API

A modern, scalable financial management API built with **Clean Architecture** principles, following **SOLID** design patterns, **Domain-Driven Design (DDD)**, **Test-Driven Development (TDD)**, and **DRY** principles.

![Java](https://img.shields.io/badge/Java-24-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-green)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![License](https://img.shields.io/badge/license-MIT-blue)

## 📋 Table of Contents

- [🏗️ Architecture Overview](#-architecture-overview)
- [🎯 Design Principles](#-design-principles)
- [🛠️ Technology Stack](#️-technology-stack)
- [🚀 Quick Start](#-quick-start)
- [📁 Project Structure](#-project-structure)
- [🔧 Configuration](#-configuration)
- [🧪 Testing Strategy](#-testing-strategy)
- [📊 API Documentation](#-api-documentation)
- [🐳 Docker Setup](#-docker-setup)
- [🔄 Development Workflow](#-development-workflow)
- [📈 Monitoring & Observability](#-monitoring--observability)
- [🤝 Contributing](#-contributing)

## 🏗️ Architecture Overview

This project implements **Clean Architecture** (also known as Hexagonal Architecture) to ensure:

- **Independence of Frameworks**: The architecture doesn't depend on the existence of some library of feature-laden software
- **Testability**: Business rules can be tested without the UI, database, web server, or any other external element
- **Independence of UI**: The UI can change easily, without changing the rest of the system
- **Independence of Database**: Business rules are not bound to the database
- **Independence of External Agencies**: Business rules don't know anything about the outside world

### Architecture Layers

```
┌─────────────────────────────────────────┐
│              Controllers                │ ← Interface Adapters
├─────────────────────────────────────────┤
│               Use Cases                 │ ← Application Business Rules
├─────────────────────────────────────────┤
│               Entities                  │ ← Enterprise Business Rules
└─────────────────────────────────────────┘
│              External                   │ ← Frameworks & Drivers
│         (Database, Web, etc.)           │
└─────────────────────────────────────────┘
```

## 🎯 Design Principles

### SOLID Principles

- **S** - Single Responsibility Principle: Each class has one reason to change
- **O** - Open/Closed Principle: Open for extension, closed for modification
- **L** - Liskov Substitution Principle: Objects should be replaceable with instances of their subtypes
- **I** - Interface Segregation Principle: Many client-specific interfaces are better than one general-purpose interface
- **D** - Dependency Inversion Principle: Depend on abstractions, not concretions

### Domain-Driven Design (DDD)

- **Ubiquitous Language**: Common language between developers and domain experts
- **Bounded Contexts**: Clear boundaries between different parts of the domain
- **Aggregates**: Consistency boundaries for business rules
- **Value Objects**: Immutable objects that describe aspects of the domain
- **Domain Events**: Capture business events that domain experts care about

### Test-Driven Development (TDD)

- **Red-Green-Refactor**: Write failing test → Make it pass → Refactor
- **Unit Tests**: Test individual components in isolation
- **Integration Tests**: Test component interactions
- **End-to-End Tests**: Test complete user workflows

### DRY (Don't Repeat Yourself)

- Shared utilities and common functionality
- Reusable components and services
- Configuration externalization
- Template patterns for common operations

## 🛠️ Technology Stack

### Core Technologies
- **Java 24**: Latest LTS version with modern language features
- **Spring Boot 3.5.5**: Enterprise-grade framework
- **Spring Security**: Authentication and authorization
- **Spring Data JPA**: Data persistence layer
- **MySQL**: Relational database
- **Flyway**: Database migration tool

### Testing & Quality
- **JUnit 5**: Unit testing framework
- **Testcontainers**: Integration testing with real dependencies
- **Spring Boot Test**: Comprehensive testing support
- **Mockito**: Mocking framework

### DevOps & Monitoring
- **Docker Compose**: Local development environment
- **Spring Boot Actuator**: Production-ready monitoring
- **Maven**: Build and dependency management

## 🚀 Quick Start

### Prerequisites
- Java 24+
- Maven 3.8+
- Docker & Docker Compose
- Git

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/cashly-api.git
   cd cashly-api
   ```

2. **Start the database**
   ```bash
   docker-compose up -d mysql
   ```

3. **Build and run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Verify the application**
   ```bash
   curl http://localhost:8080/actuator/health
   ```

## 📁 Project Structure

```
src/
├── main/java/com/cashly/cashly_api/
│   ├── domain/                     # Domain Layer (Entities, Value Objects, Domain Services)
│   │   ├── entities/              # Core business entities
│   │   ├── valueobjects/          # Immutable value objects
│   │   ├── services/              # Domain services
│   │   └── events/                # Domain events
│   ├── application/               # Application Layer (Use Cases, DTOs)
│   │   ├── usecases/              # Application use cases
│   │   ├── ports/                 # Interfaces for external dependencies
│   │   └── dto/                   # Data Transfer Objects
│   ├── infrastructure/            # Infrastructure Layer (Adapters)
│   │   ├── persistence/           # Database adapters
│   │   ├── web/                   # REST controllers
│   │   ├── security/              # Security configurations
│   │   └── config/                # Application configurations
│   └── shared/                    # Shared utilities (DRY principle)
│       ├── exceptions/            # Common exceptions
│       ├── utils/                 # Utility classes
│       └── constants/             # Application constants
├── test/java/                     # Test files (TDD approach)
│   ├── unit/                      # Unit tests
│   ├── integration/               # Integration tests
│   └── e2e/                       # End-to-end tests
└── resources/
    ├── application.properties     # Configuration
    └── db/migration/              # Flyway migrations
```

### Layer Responsibilities

#### 🏛️ Domain Layer
- **Entities**: Core business objects with identity
- **Value Objects**: Immutable objects representing descriptive aspects
- **Domain Services**: Business logic that doesn't belong to a single entity
- **Domain Events**: Capture important business events

#### 🎯 Application Layer
- **Use Cases**: Orchestrate domain objects to fulfill business requirements
- **Ports**: Interfaces that define contracts with external systems
- **DTOs**: Data transfer objects for communication between layers

#### 🔌 Infrastructure Layer
- **Persistence**: Database implementations and repositories
- **Web**: REST API controllers and request/response handling
- **Security**: Authentication and authorization mechanisms
- **Configuration**: Application setup and external service integrations

## 🔧 Configuration

### Database Configuration
```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/cashly_db
spring.datasource.username=${DB_USER:cashly_user}
spring.datasource.password=${DB_PASSWORD:cashly_pass}

# JPA
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# Flyway
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
```

### Security Configuration
```properties
# OAuth2
spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}

# JWT
jwt.secret=${JWT_SECRET:your-secret-key}
jwt.expiration=${JWT_EXPIRATION:86400}
```

### Environment Variables
```bash
# Required for production
export DB_USER=your_db_user
export DB_PASSWORD=your_db_password
export JWT_SECRET=your_jwt_secret
export GOOGLE_CLIENT_ID=your_google_client_id
export GOOGLE_CLIENT_SECRET=your_google_client_secret
```

## 🧪 Testing Strategy

Our testing approach follows the **Testing Pyramid** and **TDD** principles:

### Unit Tests (70%)
```bash
# Run unit tests
./mvnw test -Dtest="**/*UnitTest"
```

### Integration Tests (20%)
```bash
# Run integration tests
./mvnw test -Dtest="**/*IntegrationTest"
```

### End-to-End Tests (10%)
```bash
# Run E2E tests
./mvnw test -Dtest="**/*E2ETest"
```

### Test Coverage
```bash
# Generate coverage report
./mvnw jacoco:report
```

### Testing Best Practices

1. **Test Naming Convention**
   ```java
   @Test
   void should_CreateAccount_When_ValidDataProvided() {
       // Given
       // When
       // Then
   }
   ```

2. **Test Structure (AAA Pattern)**
   - **Arrange**: Set up test data and conditions
   - **Act**: Execute the operation being tested
   - **Assert**: Verify the expected outcome

3. **Test Data Builders**
   ```java
   public class AccountTestDataBuilder {
       public static Account.Builder validAccount() {
           return Account.builder()
               .id(UUID.randomUUID())
               .name("Test Account")
               .balance(Money.of(100.00, "USD"));
       }
   }
   ```

## 📊 API Documentation

### Health Check
```http
GET /actuator/health
```

### Authentication Endpoints
```http
POST /api/v1/auth/login
POST /api/v1/auth/register
POST /api/v1/auth/refresh
DELETE /api/v1/auth/logout
```

### Account Management
```http
GET    /api/v1/accounts
POST   /api/v1/accounts
GET    /api/v1/accounts/{id}
PUT    /api/v1/accounts/{id}
DELETE /api/v1/accounts/{id}
```

### Transaction Management
```http
GET    /api/v1/transactions
POST   /api/v1/transactions
GET    /api/v1/transactions/{id}
GET    /api/v1/accounts/{accountId}/transactions
```

### OpenAPI Documentation
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI Spec**: http://localhost:8080/v3/api-docs

## 🐳 Docker Setup

The project includes three specialized Dockerfiles for different environments:

### Dockerfile Overview

| Dockerfile | Purpose | Use Case | Features |
|------------|---------|----------|----------|
| `Dockerfile.dev` | Development | Local development with hot-reload | Debug port (5005), Spring DevTools, development tools |
| `Dockerfile.test` | Testing | Running tests in isolated environment | Testcontainers support, coverage reports |
| `Dockerfile.prod` | Production | Production deployment | Optimized image, security hardening, health checks |

### Development Environment

Run the application with hot-reload and debugging capabilities:

```bash
# Build and run development container
docker build -f Dockerfile.dev -t cashly-api:dev .
docker run -p 8080:8080 -p 5005:5005 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/cashly_db \
  -e SPRING_DATASOURCE_USERNAME=cashly_user \
  -e SPRING_DATASOURCE_PASSWORD=cashly_pass \
  cashly-api:dev

# Using Docker Compose (recommended)
docker-compose up -d
```

**Features:**
- Hot-reload with Spring Boot DevTools
- Remote debugging on port 5005
- Development tools pre-installed
- Connected to local MySQL database

### Testing Environment

Run all tests in an isolated container:

```bash
# Build test container
docker build -f Dockerfile.test -t cashly-api:test .

# Run all tests with coverage
docker run --rm \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -v $(pwd)/target:/app/target \
  cashly-api:test

# Run specific test suites
docker run --rm cashly-api:test ./mvnw test -Dtest="**/*UnitTest"
docker run --rm cashly-api:test ./mvnw test -Dtest="**/*IntegrationTest"
docker run --rm cashly-api:test ./mvnw test -Dtest="**/*E2ETest"

# Using Docker Compose
docker-compose up app-test
```

**Features:**
- Testcontainers support for integration tests
- JaCoCo coverage reports
- Isolated test environment
- Docker socket mounted for Testcontainers

### Production Deployment

Deploy the application with optimized production settings:

```bash
# Build production image
docker build -f Dockerfile.prod -t cashly-api:prod .

# Run production container
docker run -d \
  --name cashly-api \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql-host:3306/cashly_db \
  -e SPRING_DATASOURCE_USERNAME=prod_user \
  -e SPRING_DATASOURCE_PASSWORD=secure_password \
  -e JWT_SECRET=your_jwt_secret \
  --restart unless-stopped \
  --health-cmd="wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1" \
  --health-interval=30s \
  --health-timeout=3s \
  --health-retries=3 \
  cashly-api:prod

# View logs
docker logs -f cashly-api

# Check health
docker inspect --format='{{.State.Health.Status}}' cashly-api
```

**Features:**
- Multi-stage build (smaller image size)
- Non-root user for security
- G1GC garbage collector
- Health checks included
- Production JVM optimizations
- Minimal Alpine-based runtime

### Docker Compose Configuration

The project includes a complete `compose.yaml` with services for all environments:

```bash
# Start MySQL only
docker-compose up -d mysql

# Start development environment
docker-compose up -d app

# Run tests
docker-compose up app-test

# View logs
docker-compose logs -f app

# Stop all services
docker-compose down

# Clean up volumes
docker-compose down -v
```

### Environment Variables

Configure the following environment variables for each environment:

**Development:**
```bash
SPRING_PROFILES_ACTIVE=dev
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/cashly_db
SPRING_DATASOURCE_USERNAME=cashly_user
SPRING_DATASOURCE_PASSWORD=cashly_pass
```

**Testing:**
```bash
SPRING_PROFILES_ACTIVE=test
TESTCONTAINERS_RYUK_DISABLED=false
```

**Production:**
```bash
SPRING_PROFILES_ACTIVE=prod
SPRING_DATASOURCE_URL=jdbc:mysql://prod-host:3306/cashly_db
SPRING_DATASOURCE_USERNAME=prod_user
SPRING_DATASOURCE_PASSWORD=secure_password
JWT_SECRET=your_secure_jwt_secret
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
```

### Best Practices

1. **Never commit sensitive data**: Use `.env` files (added to `.gitignore`) for local development
2. **Use secrets management**: For production, use Docker secrets or external secret managers
3. **Health checks**: Always configure health checks for production containers
4. **Resource limits**: Set memory and CPU limits in production
5. **Non-root user**: All production containers run as non-root user
6. **Logging**: Use centralized logging for production containers

### Production Deployment Example with Docker Compose

Create a `docker-compose.prod.yaml`:

```yaml
version: '3.8'
services:
  cashly-api:
    build:
      context: .
      dockerfile: Dockerfile.prod
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - SPRING_DATASOURCE_URL=${DB_URL}
      - SPRING_DATASOURCE_USERNAME=${DB_USER}
      - SPRING_DATASOURCE_PASSWORD=${DB_PASS}
    secrets:
      - jwt_secret
    deploy:
      replicas: 2
      resources:
        limits:
          cpus: '1.0'
          memory: 1G
        reservations:
          cpus: '0.5'
          memory: 512M
    healthcheck:
      test: ["CMD", "wget", "--no-verbose", "--tries=1", "--spider", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 3s
      retries: 3

secrets:
  jwt_secret:
    external: true
```

## 🔄 Development Workflow

### Git Flow
1. **Feature Development**
   ```bash
   git checkout -b feature/new-feature
   # Make changes following TDD
   git commit -m "feat: add new feature"
   git push origin feature/new-feature
   ```

2. **Pull Request Process**
   - Create PR with detailed description
   - Ensure all tests pass
   - Code review by team members
   - Merge to main branch

### Code Quality Standards
- **Checkstyle**: Code style enforcement
- **SpotBugs**: Static analysis for bug detection
- **SonarQube**: Code quality metrics
- **Test Coverage**: Minimum 80% coverage required

### Continuous Integration
```yaml
# .github/workflows/ci.yml
name: CI
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 24
        uses: actions/setup-java@v3
        with:
          java-version: '24'
      - name: Run tests
        run: ./mvnw clean test
      - name: Generate coverage
        run: ./mvnw jacoco:report
```

## 📈 Monitoring & Observability

### Application Metrics
- **Health Checks**: `/actuator/health`
- **Metrics**: `/actuator/metrics`
- **Performance**: `/actuator/prometheus`
- **Tracing**: Distributed tracing with Micrometer

### Logging Strategy
```java
// Structured logging with MDC
@Slf4j
public class TransactionService {
    public void processTransaction(Transaction transaction) {
        MDC.put("transactionId", transaction.getId().toString());
        MDC.put("userId", transaction.getUserId().toString());
        
        log.info("Processing transaction for amount: {}", transaction.getAmount());
        
        // Business logic
        
        log.info("Transaction processed successfully");
        MDC.clear();
    }
}
```

### Performance Monitoring
- **Application Performance Monitoring (APM)**
- **Database Performance Monitoring**
- **Custom Business Metrics**
- **Error Tracking and Alerting**

## 🤝 Contributing

We welcome contributions! Please follow these guidelines:

### Development Setup
1. Fork the repository
2. Create a feature branch
3. Follow TDD practices
4. Ensure all tests pass
5. Submit a pull request

### Code Standards
- Follow Clean Code principles
- Implement SOLID design patterns
- Write comprehensive tests
- Document public APIs
- Use conventional commit messages

### Commit Message Format
```
type(scope): description

[optional body]

[optional footer]
```

**Types**: feat, fix, docs, style, refactor, test, chore

## 📚 Additional Resources

- [Clean Architecture by Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Domain-Driven Design](https://martinfowler.com/tags/domain%20driven%20design.html)
- [SOLID Principles](https://en.wikipedia.org/wiki/SOLID)
- [Test-Driven Development](https://martinfowler.com/bliki/TestDrivenDevelopment.html)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

Built with ❤️ using Clean Architecture principles and modern Java technologies.
