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
# Spring Boot Configuration
server.port=8080

# Database Configuration
#spring.datasource.url=jdbc:postgresql://localhost:5432/product
#spring.datasource.username=<your-db-username>
#spring.datasource.password=<your-db-password>

# Swagger Configuration
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html

# Logging Configuration
logging.level.org.springframework=INFO
logging.level.com.simplyminds.productservice=DEBUG

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
