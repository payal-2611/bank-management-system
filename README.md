# Bank Management System

Bank Management System is a Spring Boot REST API project developed to manage banks, bank accounts, addresses, and account-related operations.

The project uses Spring Data JPA and Hibernate for database operations and PostgreSQL for data storage. APIs are tested using Postman.

## Tech Stack

* Java 21
* Spring Boot
* Spring Data JPA
* Hibernate
* PostgreSQL
* Maven
* Postman
* Git & GitHub

## Features

### Bank Management

* Add bank details
* Get all banks
* Get bank by ID
* Update bank details
* Delete bank
* Search bank details
* Manage bank IFSC code
* Manage branch and contact details

### Address Management

* Add address details
* Get address details
* Update address details
* Delete address
* Manage bank address

### Account Management

* Create bank account
* Get account details
* Get account by ID
* Update account details
* Delete account
* Manage account type
* Deposit amount
* Withdraw amount
* Transfer amount

### Account Types

* SAVINGS
* CURRENT
* SALARY
* FIXED_DEPOSIT

## Project Structure

```text
src/main/java
└── jsp.springboot
    ├── controller
    ├── service
    ├── repository
    ├── entity
    ├── dto
    └── exception
```

The project follows a layered architecture:

```text
Controller → Service → Repository → PostgreSQL
```

## Entity Relationship

```text
Bank
 │
 ├── 1 : 1 ── Address
 │
 └── 1 : Many ── Account
```

A bank can have multiple accounts, while a bank has an associated address.

## Database

The project uses PostgreSQL.

Configure the database connection in:

```text
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/LibDB
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Use your own PostgreSQL username and password.

## Running the Project

1. Clone the repository.
2. Import the project as a Maven project in Eclipse or IntelliJ IDEA.
3. Configure PostgreSQL.
4. Update `application.properties`.
5. Run the Spring Boot application.
6. Test the APIs using Postman.

The application runs on:

```text
http://localhost:8080
```

## API Endpoints

### Bank

```text
POST    /bank
GET     /bank
GET     /bank/{id}
PUT     /bank/{id}
DELETE  /bank/{id}
```

### Account

```text
POST    /account
GET     /account
GET     /account/{id}
PUT     /account/{id}
DELETE  /account/{id}
```

### Address

```text
POST    /address
GET     /address
GET     /address/{id}
PUT     /address/{id}
DELETE  /address/{id}
```

Additional APIs are available for deposit, withdrawal, transfer, and account-related operations.

## Validation

Bank IFSC code is validated using the format:

```text
AAAA0XXXXXX
```

The project also includes validation for bank and account-related fields.

## Testing

The REST APIs were tested using Postman.

Operations tested include:

* GET
* POST
* PUT
* DELETE
* Deposit
* Withdrawal
* Transfer

## What I Worked On

* Developed REST APIs using Spring Boot
* Implemented CRUD operations
* Connected the application with PostgreSQL
* Used Spring Data JPA and Hibernate
* Implemented entity relationships
* Implemented bank and account management
* Implemented deposit and withdrawal operations
* Implemented account-to-account transfer
* Added validation and exception handling
* Tested APIs using Postman

## Author

**Payal Sahu**

B.Tech - Computer Science and Engineering

GitHub: `payal-2611`
