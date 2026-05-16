# Employee Registry System (ERS)

**Employee Registry System (ERS)** is a desktop application written in Java for the comprehensive management of employees and employee groups (departments/teams). The application features a Graphical User Interface (GUI) built with JavaFX, database integration via JPA/Hibernate, and network communication with an external server (e.g., Spring Boot) using a REST API client.

## 🌟 Core Features

* **Employee Management (CRUD)**: Add, edit, and remove employees. Editable fields include first name, last name, birth year, salary, and employment condition (e.g., `PRESENT`, `DELEGATION`, `SICK_LEAVE`, `ABSENT`).
* **Group Management**: Create new employee groups with a defined maximum capacity, assign employees to them, and remove groups.
* **Group Monitoring**: Automatically calculates and displays the fill percentage of a specific employee group.
* **Rating System**: Assign ratings (from 0 to 6) to specific groups along with a date and a comment. The system tracks the average grade and the number of ratings for each team in a dedicated statistics table.
* **Sorting and Filtering**:
* Search for employees by their last name.
* Sort employee groups alphabetically by name, by the number of employees, or by their fill percentage.




* 
**Data Export**: Export the current employee view to a CSV file (`exports/employees.csv`) using the "Export to CSV" button.


* 
**External API Integration**: Send complete data payloads (employees, groups, and rates in JSON format) to an external Spring Boot backend using the "Send to SpringBoot" button.



## 🛠 Technologies and Tools

* **Language**: Java
* **GUI**: JavaFX (using FXML patterns and editable `TableView` components)
* **Database / ORM**: Jakarta Persistence API (JPA), Hibernate, and an in-memory H2 Database.
* **HTTP Client**: Built-in `java.net.http.HttpClient` for REST API communication.
* **Backend Integration**: Spring Boot (REST controllers and Spring Data JPA repositories) for handling incoming data.

## 🗂 Main Class Structure

* `EmployeeApp` / `EmployeeAppController`: The main JavaFX application class and controller handling UI logic, TableViews, filtering, and interface events.
* `Employee`: A JPA entity representing a single employee.
* `ClassEmployee`: A JPA entity representing an employee group or team.
* `Rate` & `GroupRatingStats`: Data models for group ratings and statistical aggregation.
* `ApiClient`: A class responsible for network communication and bulk-sending JSON data from the application to the external REST API.
* `RateController`: A Spring Boot REST controller endpoint (`/api/rates`) for processing incoming rate data.

## 🚀 Getting Started

1. **Prerequisites**:
* Java Development Kit (JDK) 11 or newer (required for `HttpClient` and JavaFX).
* (Optional) A running Spring Boot server accepting requests at `http://localhost:8080` for the external API export features to work properly.


2. **Database Configuration**:
* The application is pre-configured to use an in-memory H2 database (`jdbc:h2:mem:testdb`) via the `persistence.xml` file. No external database installation is required to run and test the GUI.


3. **Running the Application**:
* Run the project from your IDE (e.g., IntelliJ IDEA, Eclipse) by configuring JavaFX support, or use build tools like Maven/Gradle to execute the application (e.g., `mvn javafx:run`).



## 📝 Developer Notes

The project automatically loads a set of sample data (employees, groups, and sample ratings) upon startup for demonstration purposes. It connects directly to the `EntityManager` from the controller to simplify the architecture for this specific desktop use case.
