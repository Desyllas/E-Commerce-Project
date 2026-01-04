# E-Commerce Spring Boot API

## Description
Personal E-Commerce REST API built with **Spring Boot**.  
It provides functionality for managing users, products, categories, and orders.  
JWT-based authentication and role-based authorization are implemented for secure access.

---

## Teck Stack
- Java 17  
- Spring Boot  
- Spring Security (JWT authentication)  
- Spring Data JPA / Hibernate  
- PostgreSql 
- Maven  
- JUnit 5 + Mockito (unit tests)  

---

## Features
- User registration and login with JWT authentication  
- Role-based access control (USER / ADMIN)  
- CRUD operations for Products and Categories
- Order creation and management  
- Exception handling with custom error responses  
- Interactive API documentation with **Swagger UI**

## API Documentation
Interactive API documentation is available via Swagger UI:
http://localhost:8080/swagger-ui.html

---

## Getting Started

### Prerequisites
- Java 17+  
- Maven 3.8+  
- (Optional) H2 or any relational database  

## Run the application
mvn spring-boot:run

The API will be available at: http://localhost:8080

## Sample Data

To quickly explore the API, the project comes with **sample data** preloaded via `data.sql`:

### **Users**
| Role  | Email               | Password     |
|-------|-------------------|-------------|
| Admin | admin@example.com  | password123 |
| User  | user@example.com   | password123 |

> You can log in using these credentials to obtain a JWT for testing protected endpoints.

### **Categories**
- Electronics  
- Books  

### **Products**
| ID | Name       | Brand   | Category     | Price  | Quantity |
|----|-----------|---------|-------------|--------|---------|
| 1  | Laptop     | BrandX  | Electronics | 1200.0 | 10      |
| 2  | Headphones | BrandY  | Electronics | 250.0  | 20      |
| 3  | Novel      | AuthorZ | Books       | 15.0   | 50      |

### **Orders**
- User `user@example.com` has 1 order containing:
  - 1 Laptop  
  - 1 Headphones  
  - Total: 1450.0

### **How to use**
1. Start the application (`mvn spring-boot:run`)  
2. Login with one of the sample users to obtain a JWT: `/api/auth/login`  
3. Use the token in `Authorization: Bearer <token>` headers for protected endpoints  
4. Explore API endpoints using [Swagger UI](http://localhost:8080/swagger-ui.html)  

> The `data.sql` script is automatically loaded by Spring Boot at startup, so no additional setup is required.

