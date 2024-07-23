## README

### Juice List Management Application

**Overview**
This Spring MVC application provides a web interface for managing a list of juices. The application utilizes Spring Boot for rapid development, Spring Security for authentication and authorization, Flyway for database migrations, MySQL as the database, Lombok for code generation, and OpCSV for CSV operations. Spring Validation is employed for data validation and pagination is implemented for efficient data retrieval.

**Features**
* Manages a list of juices
* Stores juice data in MySQL database
* Utilizes Spring Boot for rapid development
* Implements Spring Security for authentication and authorization
* Employs Flyway for database migrations
* Leverages Lombok for code generation
* Uses OpCSV for CSV operations
* Validates user input with Spring Validation
* Supports pagination for efficient data retrieval


**Application Structure**
* **Entity:** Defines the Juice entity with its properties.
* **Repository:** Provides data access methods for the Juice entity.
* **Service:** Handles business logic related to juices.
* **Controller:** Exposes REST endpoints for managing juices.
* **Security:** Configures Spring Security for authentication and authorization.
* **Validation:** Defines validation constraints for juice data.
* **Pagination:** Implements pagination for juice list retrieval.



**Testing**
Unit tests are included using Mockito for mocking dependencies.



**Additional Notes**
* 
