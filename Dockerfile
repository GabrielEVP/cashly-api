# Multi-stage build for production

# Stage 1: Build the application
FROM maven:3.9.9-amazoncorretto-24 AS builder

WORKDIR /app

# Install required tools
RUN yum update -y && yum install -y tar gzip wget && yum clean all

# Copy Maven configuration files
COPY pom.xml ./
COPY .mvn .mvn
COPY mvnw ./

# Download dependencies (this layer will be cached)
RUN chmod +x ./mvnw && ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application (skip tests in production build)
RUN ./mvnw clean package -DskipTests -Dmaven.test.skip=true

# Stage 2: Create the runtime image
FROM amazoncorretto:24-alpine

WORKDIR /app

# Install wget for health checks
RUN apk add --no-cache wget

# Create non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring

# Copy the built JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Change ownership to non-root user
RUN chown -R spring:spring /app

USER spring:spring

# Expose application port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Run the application with production JVM options
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+UseG1GC", \
    "-XX:+UseStringDeduplication", \
    "-XX:+OptimizeStringConcat", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-Dspring.profiles.active=prod", \
    "-jar", \
    "app.jar"]
