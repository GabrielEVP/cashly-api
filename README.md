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
   git clone https://github.com/GabrielEVP/cashly-api.git
   cd cashly-api
   ```

2. **Choose your environment and start:**

   **🚀 Production (Default):**
   ```bash
   docker compose up --build
   ```

   **🔧 Development (Hot-reload + Debug):**
   ```bash
   DOCKERFILE=Dockerfile.dev SPRING_PROFILES_ACTIVE=dev docker compose up --build
   ```

   **🧪 Testing:**
   ```bash
   docker compose --profile test up --build
   ```

3. **Verify the application**
   ```bash
   curl http://localhost:8080/actuator/health
   # Response: {"status":"UP"}
   ```

> 💡 **Pro Tip**: For development, set environment variables in `.env` file:
> ```bash
> echo "DOCKERFILE=Dockerfile.dev" > .env
> echo "SPRING_PROFILES_ACTIVE=dev" >> .env
> docker compose up --build
> ```

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

The project provides **three optimized Docker environments** for different use cases. Each environment is containerized and ready to use with simple commands.

### 📋 Available Environments

| Environment | Dockerfile | Profile | Description | Use Case |
|-------------|------------|---------|-------------|----------|
| **🚀 Production** | `Dockerfile` | `prod` | Optimized production build | **Deployment, Production** |
| **🔧 Development** | `Dockerfile.dev` | `dev` | Hot-reload + Debug support | **Local development** |
| **🧪 Testing** | `Dockerfile.test` | `test` | Isolated test environment | **Automated testing** |

---

## 🚀 Production Environment (Default)

**Best for**: Production deployment, staging, performance testing

```bash
# Start production environment (DEFAULT)
docker compose up --build

# Run in background
docker compose up -d --build

# Check health
curl http://localhost:8080/actuator/health
```

**✨ Features:**
- ✅ Multi-stage optimized build
- ✅ Minimal runtime image (Alpine)
- ✅ Non-root user security
- ✅ Production JVM optimizations
- ✅ Health checks enabled
- ✅ No development tools

---

## 🔧 Development Environment

**Best for**: Local development, debugging, hot-reload

```bash
# Start development environment
DOCKERFILE=Dockerfile.dev SPRING_PROFILES_ACTIVE=dev docker compose up --build

# Or create .env file with:
echo "DOCKERFILE=Dockerfile.dev" > .env
echo "SPRING_PROFILES_ACTIVE=dev" >> .env
docker compose up --build
```

**✨ Features:**
- ✅ **Hot Reload** - Changes reflect automatically
- ✅ **Remote Debug** - Port 5005 for IDE debugging
- ✅ **LiveReload** - Port 35729 for frontend
- ✅ **Development Tools** - vim, git, wget included
- ✅ **Source Mounting** - Live code synchronization
- ✅ **Dev Profile** - Development database settings

**🔌 Debug Setup (IntelliJ/VS Code):**
```
Host: localhost
Port: 5005
Transport: Socket
Debugger mode: Attach
```

---

## 🧪 Testing Environment

**Best for**: Running tests, CI/CD pipelines, test automation

```bash
# Run complete test suite
docker compose --profile test up --build

# Run tests in background and check results
docker compose --profile test up -d --build
docker compose logs app-test
```

**✨ Features:**
- ✅ **Isolated Test DB** - Separate test database (port 3307)
- ✅ **Test Profile** - Test-specific configurations
- ✅ **Maven Test Runner** - Executes complete test suite
- ✅ **CI/CD Ready** - Perfect for automated pipelines
- ✅ **No Side Effects** - Doesn't affect dev/prod data

---

## 🛠️ Container Management

### Basic Operations

```bash
# Stop all containers
docker compose down

# Stop and remove volumes (⚠️ deletes data)
docker compose down -v

# Rebuild without cache
docker compose build --no-cache

# View logs (all services)
docker compose logs -f

# View specific service logs
docker compose logs app
docker compose logs mysql
docker compose logs app-test  # for test environment
```

### Advanced Operations

```bash
# Enter container shell (development)
docker compose exec app bash

# Run specific commands in container
docker compose exec app ./mvnw clean compile
docker compose exec app ./mvnw test

# Check container resource usage
docker stats
```

---

## ⚙️ Environment Configuration

### Key Variables

| Variable | Default | Dev Override | Description |
|----------|---------|--------------|-------------|
| `DOCKERFILE` | `Dockerfile` | `Dockerfile.dev` | Which Dockerfile to use |
| `SPRING_PROFILES_ACTIVE` | `prod` | `dev` | Spring Boot profile |
| `SERVER_PORT` | `8080` | `8080` | Application port |
| `DB_NAME` | `mydatabase` | `mydatabase` | Database name |
| `DB_USER` | `myuser` | `myuser` | Database user |
| `DB_PASSWORD` | `secret` | `secret` | Database password |
| `JWT_SECRET` | `default...` | `default...` | JWT signing secret |

### Configuration Files

```bash
# Copy environment template
cp .env.example .env

# Edit configuration for your needs
nano .env

# Example .env for development:
DOCKERFILE=Dockerfile.dev
SPRING_PROFILES_ACTIVE=dev
LOG_LEVEL=DEBUG
```

---

## 🎯 Quick Reference Commands

### Development Workflow

```bash
# 1. Clone and setup
git clone https://github.com/GabrielEVP/cashly-api.git
cd cashly-api

# 2. Start development environment
DOCKERFILE=Dockerfile.dev SPRING_PROFILES_ACTIVE=dev docker compose up --build

# 3. Make changes and see them automatically reload!
# Edit files in src/ - changes will be reflected immediately

# 4. Run tests
docker compose --profile test up --build
```

### Production Deployment

```bash
# 1. Production build and run
docker compose up -d --build

# 2. Check health
curl http://localhost:8080/actuator/health

# 3. Monitor logs
docker compose logs -f app

# 4. Stop when needed
docker compose down
```

### Troubleshooting

```bash
# Reset everything
docker compose down -v
docker system prune -f

# Check container status
docker compose ps

# View detailed logs
docker compose logs app --tail=50

# Enter container for debugging
docker compose exec app bash
```

#### Key Environment Variables

```bash
# Container profiles (comma-separated)
DOCKER_PROFILES=app,database

# Database configuration
DB_HOST=mysql
DB_PORT=3306
DB_NAME=mydatabase
DB_USER=myuser
DB_PASSWORD=secret

# Application configuration
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=dev

# JWT configuration
JWT_SECRET=your-secret-key
JWT_ACCESS_TOKEN_EXPIRATION=900000
```

### Development Workflow

#### 1. First Time Setup
```bash
# Clone and setup
git clone <repository>
cd cashly-api
cp .env.example .env

# Start development environment
docker compose --profile app --profile database up --build
```

#### 2. Daily Development
```bash
# Start services
docker compose --profile app --profile database up -d

# View logs
docker compose logs -f app

# Run tests
docker compose --profile test up --build

# Stop when done
docker compose down
```

#### 3. Database Only Development
```bash
# Start only database (run app from IDE)
docker compose --profile database up -d

# Your IDE connects to: localhost:3306
# Database: mydatabase
# User: myuser
# Password: secret
```

### Container Features

#### Development Container (`app` profile)
- Hot-reload with Spring Boot DevTools
- Debug port exposed (5005)
- Volume mounted for live code changes
- Connected to development database

#### Database Container (`database` profile)
- MySQL 8.0 with persistent storage
- Initialization scripts support
- Development user and database pre-configured
- Exposed on localhost:3306

#### Test Container (`test` profile)
- Isolated test database (port 3307)
- Testcontainers support
- Coverage reports generation
- Clean environment for each test run

### Troubleshooting

#### Common Issues

**Port conflicts:**
```bash
# Check what's using port 3306
lsof -i :3306

# Change port in .env file
DB_PORT=3307
```

**Permission issues:**
```bash
# Fix Docker permissions
sudo usermod -aG docker $USER
logout # and login again
```

**Database connection issues:**
```bash
# Check database logs
docker compose logs mysql

# Verify database is ready
docker compose exec mysql mysql -u myuser -p mydatabase
```

#### Health Checks

```bash
# Check container status
docker compose ps

# Check application health
curl http://localhost:8080/actuator/health

# Check database connectivity
docker compose exec mysql mysqladmin ping -h localhost -u myuser -p
```
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

## 🚀 Docker Quick Reference Card

### 🎯 One-Command Setup for Each Environment:

```bash
# 🚀 PRODUCTION (Default - Optimized for deployment)
docker compose up --build

# 🔧 DEVELOPMENT (Hot-reload + Debug)
DOCKERFILE=Dockerfile.dev SPRING_PROFILES_ACTIVE=dev docker compose up --build

# 🧪 TESTING (Automated test suite)
docker compose --profile test up --build
```

### 🔥 Key Features by Environment:

| Feature | 🚀 Production | 🔧 Development | 🧪 Testing |
|---------|---------------|----------------|-------------|
| **Optimized Build** | ✅ Multi-stage | ❌ Single stage | ❌ Single stage |
| **Image Size** | ✅ Minimal (Alpine) | ❌ Full (Maven) | ❌ Full (Maven) |
| **Hot Reload** | ❌ Static | ✅ DevTools | ❌ N/A |
| **Debug Port** | ❌ No | ✅ Port 5005 | ❌ No |
| **Security** | ✅ Non-root user | ❌ Root user | ❌ Root user |
| **Database** | ✅ Port 3306 | ✅ Port 3306 | ✅ Port 3307 (isolated) |
| **Use Case** | Production deploy | Local coding | CI/CD pipelines |

### 💡 Pro Tips:

- **Set `.env` file for persistent dev config**:
  ```bash
  echo "DOCKERFILE=Dockerfile.dev" > .env
  echo "SPRING_PROFILES_ACTIVE=dev" >> .env
  ```

- **Check health**: `curl http://localhost:8080/actuator/health`
- **Debug setup**: Connect IDE to `localhost:5005`
- **View logs**: `docker compose logs -f app`
- **Clean reset**: `docker compose down -v && docker system prune -f`

---

Built with ❤️ using Clean Architecture principles and modern Java technologies.
