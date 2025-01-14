# Stage 1: Build Stage
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy the pom.xml and download dependencies (caching this step)
COPY target/product-service.jar product-service.jar

# Expose the port the application runs on (default: 8080)
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "product-service.jar"]
