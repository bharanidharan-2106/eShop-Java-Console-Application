# eShop Java Console Application

A console-based eShop system developed using Java, JDBC, and MySQL. The application is designed using a layered architecture (DAO, Service, Model) to reflect real-world backend application structure and best practices.

This project demonstrates core backend development skills including database connectivity, CRUD operations, modular design, and separation of concerns.

---

## Key Highlights

* Layered architecture: DAO, Service, Model, Utility
* JDBC-based integration with MySQL
* Role-based functionality: Admin and Customer
* Structured package organization following industry practices
* Custom exception handling
* Externalized database configuration using properties file

---

## Features

### Admin Module

* View all menu items
* Add new menu items
* Modify existing menu items
* Delete menu items

### Customer Module

* View available menu items
* Basic order placement flow

---

## Tech Stack

* Java
* JDBC
* MySQL
* Eclipse IDE
* Git and GitHub

---

## Project Structure

```text
src/main/java
 └── com.wipro.eshop
     ├── App.java
     ├── dao
     │   ├── CartDao.java
     │   ├── CartDaoSqlImpl.java
     │   ├── ConnectionHandler.java
     │   ├── MenuItemDao.java
     │   └── MenuItemDaoSqlImpl.java
     ├── exception
     │   └── CartEmptyException.java
     ├── model
     │   ├── Cart.java
     │   └── MenuItem.java
     ├── service
     │   ├── CartService.java
     │   ├── CartServiceImpl.java
     │   ├── MenuItemService.java
     │   └── MenuItemServiceImpl.java
     └── util
         └── DateUtil.java

src/main/resources
 └── connection.properties
```

---

## Setup and Installation

### 1. Clone the Repository

```bash
git clone https://github.com/bharanidharan-2106/eShop-Java-Console-Application.git
cd eShop-Java-Console-Application
```

### 2. Configure Database

Create a MySQL database:

```sql
CREATE DATABASE eshop;
```

Update database credentials in your local configuration file.

Note:
The `connection.properties` file is not included in this repository to protect sensitive information such as database credentials. You must create this file locally with your own configuration.

Example format:

```properties
url=jdbc:mysql://localhost:3306/eshop
username=root
password=your_password
```

---

### 3. Run the Application

* Import the project into Eclipse IDE
* Build the project
* Run the main class (`App.java`)

---

## Key Concepts Demonstrated

* JDBC connectivity and SQL integration
* CRUD operations on relational database
* DAO design pattern
* Service layer abstraction
* Exception handling using custom exceptions
* External configuration management

---

## Future Enhancements

* Full order management workflow
* User authentication and authorization
* Input validation improvements
* Logging framework integration
* Migration to Spring Boot for web-based implementation

---

## Author

Bharanidharan
GitHub: https://github.com/bharanidharan-2106

---

## Note

This project is developed for learning and demonstration purposes, focusing on backend development concepts and clean code practices.

This project was developed as part of an internship program at Wipro, Chennai, where it served as a practical implementation of core Java and database integration concepts in a structured development environment.

