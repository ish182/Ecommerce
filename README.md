# E-Commerce Website

A full-stack E-Commerce web application built with **Java, Spring Boot, Spring Security, Thymeleaf, and MySQL**. The application provides separate functionality for administrators and customers, including product management, shopping cart operations, order placement, order history, and email order confirmation.

## Features

### User Features

* User registration and login
* Secure password hashing using BCrypt
* Role-based access using Spring Security
* Browse available products
* Add products to a shopping cart
* Remove products from the cart
* Stock validation during checkout
* Place orders
* View order history
* Receive order confirmation emails

### Admin Features

* Secure admin authentication
* Add new products
* Edit existing products
* Delete products
* Upload product images
* View all customer orders
* Manage product stock and pricing

## Tech Stack

| Technology        | Purpose                          |
| ----------------- | -------------------------------- |
| Java 17           | Backend programming              |
| Spring Boot 3.5.5 | Backend framework                |
| Spring MVC        | Web request handling             |
| Spring Data JPA   | Database interaction             |
| Hibernate         | ORM                              |
| MySQL             | Database                         |
| Thymeleaf         | Server-side frontend rendering   |
| HTML/CSS          | User interface                   |
| Spring Security   | Authentication and authorization |
| BCrypt            | Password hashing                 |
| Spring Boot Mail  | Email notifications              |
| Gmail SMTP        | Email delivery                   |
| Maven             | Dependency management and build  |

## Architecture

The application follows a layered Spring Boot architecture:

```text
User
  ↓
Thymeleaf / HTML
  ↓
Controller
  ↓
Service / Business Logic
  ↓
Repository
  ↓
MySQL Database
```

Spring Security works across the authentication and authorization flow to protect user and admin resources.

## Application Flow

### 1. User Registration

A new user provides their registration details.

The application:

1. Checks whether the email already exists.
2. Hashes the password using BCrypt.
3. Assigns the default `ROLE_USER` role.
4. Stores the user in MySQL.

### 2. User Login

The user logs in using their email and password.

Spring Security:

1. Loads the user's details using `CustomUserDetailsService`.
2. Retrieves the user from MySQL through `UserRepository`.
3. Verifies the entered password against the stored BCrypt hash.
4. Authenticates the user.
5. Uses the user's role to control access.
6. Redirects the user to the appropriate dashboard.

Administrators are given access to `/admin/**`, while normal users access `/user/**`.

### 3. Product Browsing

After authentication, users can view available products from the user dashboard.

Product information is retrieved from MySQL using `ProductRepository`.

### 4. Shopping Cart

When a user clicks **Add to Cart**:

```text
User
 ↓
UserController
 ↓
ProductRepository
 ↓
MySQL
 ↓
Product
 ↓
Session Cart
```

The cart is maintained in the user's session because it represents temporary shopping state.

The cart can be modified by adding or removing products before checkout.

### 5. Checkout

When the user clicks **Checkout**:

1. The application checks whether the cart contains products.
2. The latest product information is retrieved from MySQL.
3. Product stock is checked.
4. Available stock is reduced.
5. An `Order` is created.
6. An `OrderItem` is created for each product in the order.
7. The purchase-time price and other product information are stored in the `OrderItem`.
8. The total order amount is calculated.
9. The order is saved using `OrderRepository`.
10. The session cart is cleared.
11. An order confirmation email is sent.
12. The user is redirected to the order history page.

## Order and OrderItem

A separate `OrderItem` entity is used because one order can contain multiple products.

For example:

```text
Order
 ├── OrderItem → Laptop
 ├── OrderItem → Mouse
 └── OrderItem → Keyboard
```

Each `OrderItem` stores information specific to that purchase, such as:

* Product name
* Quantity
* Purchase price
* Image information
* Associated order
* Associated product

Storing the purchase-time price is particularly important for maintaining accurate order history.

For example:

```text
Product price when purchased: ₹1,000
Current product price:        ₹1,500

Old order price:              ₹1,000
```

Changing the current product price should not change the price recorded for a previous order.

## Database Relationships

The major relationships are:

```text
User
 │
 └── Orders

Order
 │
 └── One-to-Many
       │
       └── OrderItem
              │
              └── Many-to-One
                    │
                    └── Product
```

### Order → OrderItem

One order can contain multiple order items.

### OrderItem → Product

Each order item represents a particular product included in an order.

## Authentication and Authorization

The project uses **Spring Security** for authentication and authorization.

### Authentication

Authentication verifies the identity of the user during login.

```text
Email + Password
       ↓
Spring Security
       ↓
CustomUserDetailsService
       ↓
UserRepository
       ↓
MySQL
       ↓
BCrypt password verification
       ↓
Authenticated User
```

### Authorization

After authentication, Spring Security checks the user's role.

```text
ROLE_ADMIN → /admin/**
ROLE_USER  → /user/**
```

This prevents normal users from accessing administrator functionality.

## Password Security

Passwords are not stored directly in the database.

Instead, BCrypt is used to hash passwords before storing them.

During login, Spring Security compares the entered password with the stored BCrypt hash.

## Email Confirmation

After a successful checkout, the application uses an `EmailService` based on Spring Boot's `JavaMailSender`.

```text
Checkout
   ↓
Order saved
   ↓
EmailService
   ↓
Gmail SMTP
   ↓
Order confirmation email
```

The email provides confirmation of the user's order.

## Product Images

Product images are uploaded through the admin section.

The application generates a unique filename for uploaded images and stores the image filename with the product information.

## Project Structure

```text
src/
└── main/
    ├── java/
    │   └── com/example/ecommerce/
    │       ├── Repository/
    │       │   ├── OrderRepository.java
    │       │   ├── ProductRepository.java
    │       │   └── UserRepository.java
    │       │
    │       ├── config/
    │       │   └── SecurityConfig.java
    │       │
    │       ├── controller/
    │       │   ├── AdminController.java
    │       │   ├── AuthController.java
    │       │   └── UserController.java
    │       │
    │       ├── entity/
    │       │   ├── Order.java
    │       │   ├── OrderItem.java
    │       │   ├── Product.java
    │       │   └── User.java
    │       │
    │       └── service/
    │           ├── AutoService.java
    │           ├── CustomUserDetailsService.java
    │           ├── EmailService.java
    │           └── ProductService.java
    │
    └── resources/
        ├── static/
        │   └── images/
        └── templates/
            ├── add-product.html
            ├── admin-dashboard.html
            ├── admin-orders.html
            ├── cart.html
            ├── login.html
            ├── register.html
            ├── user-dashboard.html
            └── order-histroy.html
```

## Database

The application uses MySQL with the database:

```text
ecommerce_db
```

Spring Data JPA and Hibernate are used to map Java entities to database tables and perform database operations.

The application currently uses:

```properties
spring.jpa.hibernate.ddl-auto=update
```

This allows Hibernate to update the database schema based on the entity mappings during development.

## Configuration

Sensitive configuration such as:

* Database passwords
* Email credentials
* SMTP credentials

should be provided through environment variables or another secure configuration mechanism rather than committed to source control.

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
```

## Running the Project

### Prerequisites

Make sure the following are installed:

* Java 17
* Maven
* MySQL
* Git

### Database Setup

Create the MySQL database:

```sql
CREATE DATABASE ecommerce_db;
```

Configure the database credentials in the application's configuration.

### Run the Application

Clone the repository:

```bash
git clone <repository-url>
```

Navigate to the project directory:

```bash
cd Ecommerce-master
```

Run the application using Maven:

```bash
mvn spring-boot:run
```

The application can then be accessed through the configured local server.

## Future Improvements

Possible improvements include:

* Integrating a secure online payment gateway
* Improving concurrent stock handling during checkout
* Moving uploaded images to cloud storage
* Adding product search and filtering
* Adding product categories
* Adding pagination
* Adding customer reviews and ratings
* Improving order status management
* Using database migration tools such as Flyway or Liquibase
* Moving all sensitive configuration to environment variables

## Key Learning Outcomes

Through this project, I gained practical experience with:

* Spring Boot application development
* MVC architecture
* Spring Data JPA and Hibernate
* MySQL database integration
* Entity relationships
* Spring Security
* Role-based authorization
* BCrypt password hashing
* Session-based shopping carts
* Order and order-item management
* Stock management
* Email integration using JavaMailSender
* Thymeleaf server-side rendering
* REST-style web application development

## License

This project is intended for educational and portfolio purposes.
