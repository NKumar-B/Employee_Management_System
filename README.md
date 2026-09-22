# Employee Management System Backend

A robust, enterprise-ready RESTful web service for **Employee Management**, built with **Java 17**, **Spring Boot 3.3.4**, **Spring Data JPA**, and **MySQL**. 

This application provides complete CRUD (Create, Read, Update, Delete) capabilities for managing employee records, includes data validation and custom exception handling, and features an embedded interactive web dashboard served directly from static resources.

---

## Key Features

- **Full Employee CRUD Operations**: Seamlessly create, retrieve, update, and delete employee records.
- **DTO Pattern & Layered Architecture**: Clean separation of concerns across Controllers, Services, Repositories, Entities, and DTOs using Builder design pattern.
- **Email Uniqueness Validation**: Prevents duplicate employee entries with automatic email check on create and update operations.
- **Global Exception Handling**: Centralized error handling using `@ControllerAdvice` providing structured JSON error responses (`ErrorDetails`) with timestamps and status codes.
- **Interactive Web Dashboard**: Embedded Single-Page Application (SPA) frontend served at `/` (`http://localhost:8080/`) for managing employees directly from the browser.
- **Database Persistence**: Fully configured for **MySQL** (permanent persistence) and **H2 Database** (in-memory test runtime).
- **CORS Enabled**: Pre-configured cross-origin support (`@CrossOrigin("*")`) allowing easy integration with external frontend applications (React, Angular, Vue, etc.).
- **Automated Integration Testing**: Comprehensive test coverage using JUnit 5, Spring Boot Test, and MockMvc.

---

## Technology Stack

| Component | Technology / Library |
| :--- | :--- |
| **Language** | Java 17 |
| **Framework** | Spring Boot 3.3.4 |
| **Data Access** | Spring Data JPA / Hibernate |
| **Database** | MySQL 8.0+ (Production/Dev), H2 Database (Testing) |
| **Build Tool** | Apache Maven (with Maven Wrapper `mvnw`) |
| **Web & REST** | Spring MVC, Embedded Tomcat (Port 8080) |
| **Testing** | JUnit 5, Spring Boot Test, MockMvc, Jackson |

---

## Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/employee/backend/
│   │   │   ├── controller/
│   │   │   │   ├── EmployeeController.java   # REST Endpoints (/api/v1/employees)
│   │   │   │   └── HomeController.java       # Route redirect for SPA Dashboard
│   │   │   ├── dto/
│   │   │   │   └── EmployeeDTO.java          # Data Transfer Object with Builder pattern
│   │   │   ├── entity/
│   │   │   │   └── Employee.java             # JPA Entity mapped to 'employees' table
│   │   │   ├── exception/
│   │   │   │   ├── ErrorDetails.java         # Standardized error response payload
│   │   │   │   ├── GlobalExceptionHandler.java # Global REST exception handler
│   │   │   │   └── ResourceNotFoundException.java # Custom 404 Exception
│   │   │   ├── repository/
│   │   │   │   └── EmployeeRepository.java   # Spring Data JPA Repository
│   │   │   └── service/
│   │   │       ├── EmployeeService.java      # Service Layer Interface
│   │   │       └── impl/
│   │   │           └── EmployeeServiceImpl.java # Business Logic Implementation
│   │   └── resources/
│   │       ├── static/
│   │       │   └── index.html                # Built-in Interactive Web UI
│   │       └── application.properties        # Application & Database Configuration
│   └── test/
│       └── java/com/employee/backend/
│           └── controller/
│               └── EmployeeControllerIntegrationTests.java # MockMvc Integration Tests
├── mvnw / mvnw.cmd                            # Maven Wrapper Scripts
├── pom.xml                                    # Maven Dependencies & Configuration
└── README.md                                  # Project Documentation
```

---

## Prerequisites & Setup

### Prerequisites

Ensure you have the following installed on your local environment:
- **Java Development Kit (JDK) 17** or higher.
- **MySQL Community Server 8.0+** running locally or accessible via network.
- **Git** (optional).

### Database Configuration

1. Make sure MySQL service is running on `localhost:3306`.
2. The application is configured to automatically create the database `employeedb` if it does not exist.
3. Database connection properties can be updated in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/employeedb?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=Root
```

---

## Running the Application

### Using Maven Wrapper (Windows)

```powershell
# Run the application
.\mvnw.cmd spring-boot:run
```

### Using Maven Wrapper (Linux / macOS)

```bash
# Grant execution permissions if needed
chmod +x mvnw

# Run the application
./mvnw spring-boot:run
```

The application will start on **port 8080** by default.

- **Web Dashboard**: Access [http://localhost:8080/](http://localhost:8080/) in your browser.
- **API Base Endpoint**: [http://localhost:8080/api/v1/employees](http://localhost:8080/api/v1/employees)

---

## Running Tests

Execute the full suite of integration tests using the Maven wrapper:

```powershell
.\mvnw.cmd test
```

This runs all MockMvc test cases in `EmployeeControllerIntegrationTests` covering creation, retrieval, updates, and deletion of employees.

---

## REST API Reference

Base URL: `http://localhost:8080/api/v1/employees`

| Method | Endpoint | Description | Status Code |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/employees` | Create a new employee | `201 Created` |
| `GET` | `/api/v1/employees` | Get all employees | `200 OK` |
| `GET` | `/api/v1/employees/{id}` | Get employee by ID | `200 OK` / `404 Not Found` |
| `PUT` | `/api/v1/employees/{id}` | Update employee details by ID | `200 OK` / `400 Bad Request` / `404 Not Found` |
| `DELETE` | `/api/v1/employees/{id}` | Delete employee record by ID | `200 OK` / `404 Not Found` |

---

### Request & Response Examples

#### 1. Create Employee
- **`POST /api/v1/employees`**
- **Content-Type**: `application/json`

**Sample Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "department": "Engineering",
  "salary": 75000.00
}
```

**Sample Response Body (201 Created):**
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "department": "Engineering",
  "salary": 75000.0
}
```

#### 2. Get All Employees
- **`GET /api/v1/employees`**

**Sample Response Body (200 OK):**
```json
[
  {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "department": "Engineering",
    "salary": 75000.0
  },
  {
    "id": 2,
    "firstName": "Alice",
    "lastName": "Smith",
    "email": "alice.smith@example.com",
    "department": "Human Resources",
    "salary": 68000.0
  }
]
```

#### 3. Update Employee
- **`PUT /api/v1/employees/1`**
- **Content-Type**: `application/json`

**Sample Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "department": "Senior Engineering",
  "salary": 85000.00
}
```

**Sample Response Body (200 OK):**
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "department": "Senior Engineering",
  "salary": 85000.0
}
```

#### 4. Delete Employee
- **`DELETE /api/v1/employees/1`**

**Sample Response Body (200 OK):**
```text
Employee deleted successfully!
```

#### 5. Error Response Format (404 Not Found / 400 Bad Request)
```json
{
  "timestamp": "2026-09-22T09:44:17.564",
  "message": "Employee not found with id: 99",
  "details": "uri=/api/v1/employees/99",
  "status": 404
}
```

---

## Interactive Web UI

The application includes an embedded dashboard accessible at [http://localhost:8080/](http://localhost:8080/).

Features of the built-in UI:
- Interactive dashboard statistics (Total Employees, Departments, Salary Metrics).
- Data table displaying active employee records.
- Action buttons for adding new employees, editing details, and deleting records directly via API integration.

---

## 📝 License

This project is open-source and available under the [MIT License](LICENSE).
