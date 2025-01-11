# Product Service

A microservice for managing the product catalog as part of the Inventory Management System. This service is responsible for handling all CRUD operations related to products, including creating, retrieving, updating, and deleting products.

## Features
- Add new products to the catalog.
- Retrieve a list of products with support for filtering and pagination.
- Retrieve detailed information for a specific product by ID.
- Update existing product details.
- Delete a product from the catalog.
- Real-time monitoring of product stock levels (supports reorder thresholds).

## Tech Stack

- Java: Version 17 (or specify your version)
- Spring Boot: 3.x
    - Spring Web
    - Spring Data JPA
    - Spring Actuator

- Database: MySQL
- Tools: Maven, Swagger (OpenAPI 3.0), Postman
- Cache: Redis (Optional, if used for stock monitoring)

### Steps to Run the Application

#### 1. Clone the Repository
```shell
git clone https://github.com/<your-organization>/product-service.git  

cd product-service
```

### 2. Update Application Properties
Update the application.yml or application.properties file with your database credentials:

```properties
# Spring Boot Application Configuration
spring.application.name=product-service
server.port=8080

# Database Configuration (MySQL)
spring.datasource.url=jdbc:mysql://localhost:3306/product_service?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=#replace with database password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Hibernate JPA Configuration
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Logging
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE


# Optional: Cache Configuration (if Redis is used in the future)
```

## Development Guidelines

### Code Quality

- Follow SOLID principles and use design patterns wherever applicable.
- Write unit tests for all service and repository layers.

### Commit Message Format
```shell
<ticket-ID>: Meaningful message describing the change.

Example: IOMS-1005: Updated README.md for product-service.
```

### Using profiled application properties file

#### Development Profile: application-dev.properties

```properties
# Spring Boot Application Configuration
spring.application.name=product-service-dev
server.port=8081

# Database Configuration (Development - Local MySQL)
spring.datasource.url=jdbc:mysql://localhost:3306/product_service_dev?useSSL=false&serverTimezone=UTC
spring.datasource.username=dev_user
spring.datasource.password=#database paaword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Hibernate JPA Configuration
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Logging Configuration
logging.level.root=INFO
logging.level.org.springframework.web=DEBUG

# Optional: Cache Configuration (if Redis is used in the future)
```

#### Enable Profiles in application.properties

To switch between profiles (e.g., dev, prod), you can specify the active profile in the application.properties or as a runtime parameter.

```properties
# Active Profile
spring.profiles.active=dev
```

