# Employee Management System

A simple Employee Management System built with Spring Boot, Spring MVC, Thymeleaf, Spring Data JPA, MySQL, and Flyway.

# Features

- Employee CRUD
- Search employee by name or department
- Department management
- Validation handling
- Global exception handling
- MVC architecture
- RESTful routing
- Logging with SLF4J + Logback
- Spring Profiles (dev/prod)
- Flyway database migration
- SCSS styling

# Technologies Used

- Java 17
- Spring Boot
- Spring MVC
- Spring Data JPA
- Thymeleaf
- MySQL
- Flyway
- Lombok
- Maven
- SCSS

# Project Structure

src/main/java/com/example/employeemanagement  
├── controller  
│ ├── api  
│ └── view  
├── dto  
│ ├── request  
│ └── response  
├── entity  
├── repository  
├── service  
├── exception  
├── resource  
└── config

# Database Setup

Create a MySQL database:

CREATE DATABASE employee_management;

# Configure Environment Variables (Windows)

## 1\. Open Environment Variables

Search:

Edit the system environment variables

Then:

Advanced  
→ Environment Variables

## 2\. Add New Variables

Create the following variables:

| Variable Name | Example Value                                   |
| ------------- | ----------------------------------------------- |
| DB_URL        | jdbc:mysql://localhost:3306/employee_management |
| DB_USERNAME   | root                                            |
| DB_PASSWORD   | 123456                                          |

## 3\. Restart IDE / Terminal

After adding environment variables:

- Restart IntelliJ IDEA
- Or open a new terminal

# Application Configuration

## application.yml
## application-dev.yml
## application-prod.yml


# Install Dependencies

mvn clean install

# Run Application

## Development Profile

mvn spring-boot:run -D spring-boot.run.profiles=dev

## Production Profile

mvn spring-boot:run -D spring-boot.run.profiles=prod

# Access Application

| Page          | URL                                   |
| ------------- | ------------------------------------- |
| Employee List | <http://localhost:8080/employees>     |
| Add Employee  | <http://localhost:8080/employees/new> |

# RESTful MVC Routes

| Method | URL                  | Description        |
| ------ | -------------------- | ------------------ |
| GET    | /employees           | Employee list      |
| GET    | /employees/new       | Add employee form  |
| POST   | /employees           | Create employee    |
| GET    | /employees/{id}/edit | Edit employee form |
| PUT    | /employees/{id}      | Update employee    |
| DELETE | /employees/{id}      | Delete employee    |

# Flyway Migration

Migration files location:

src/main/resources/db/migration

Example:

V1\_\_create_employee_table.sql

# Logging

Development profile enables:

- SQL logging
- DEBUG logging
- Hibernate SQL logging

Example log:

Employee created successfully with id: 1

# Build Jar File

mvn clean package

Generated jar:

target/employee-management-0.0.1-SNAPSHOT.jar

Run jar:

java -jar target/employee-management-0.0.1-SNAPSHOT.jar

# Future Improvements

- Spring Security
- JWT Authentication
- Pagination
- Docker
- Unit Testing
- API Documentation (Swagger)
- File Upload
- Cloud Deployment
# Run the following command to watch for changes in SCSS files and compile them to CSS:
npx sass --watch src/main/resources/static/scss:src/main/resources/static/css
