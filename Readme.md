Certainly! Here's your **formatted version** of the Library Management System requirement document — no content changed, just structured and indented for better readability:

---

# **D21\_S2\_A1\_Spring Boot-based Web Application for a Simplified Library Management System Using Spring Security**

---

## **Overview**

Develop a Spring Boot-based web application for a Library Management System with basic Spring Security features to manage book records, users, and borrowing operations.

The system should enable users to:

* Register
* Borrow books
* View available books
* Manage user accounts

There should also be **role-based access control** with roles such as:

* `"USER"` – can borrow books
* `"ADMIN"` – can manage books and users

---

## **Functional Requirements**

---

### **1. User Registration and Authentication**

#### **User Registration**

* Endpoint: `/users/register`
* HTTP Method: `@PostMapping`
* Function: Allow users to register a new account
* Requirements:

    * Accept a `User` object in the request body
    * Require a unique `username` and `password`
    * Assign roles: `"USER"` or `"ADMIN"`
    * Encode passwords using Spring Security
    * Return a message in the response

#### **User Login**

* Endpoint: `/users/login`
* HTTP Method: `@PostMapping`
* Function: Allow users to log in to the system
* Requirements:

    * Accept an `AuthRequest` object in request body
    * Return an `AuthResponse` object containing a **JWT token**

> **Note**: `AuthRequest` and `AuthResponse` classes are already provided under `/src/main/java/com/wecp/library_management_system_jwt/dto/`

#### **Role-Based Access Control**

* `"USER"` role:

    * Can borrow books
    * Can view book details

* `"ADMIN"` role:

    * Can add, update, delete books
    * Can view all users

---

### **2. Book Management (Admin Role)**

#### **a. Create Book**

* Endpoint: `/books`
* HTTP Method: `@PostMapping`
* Access: `ADMIN`
* Function: Add new books
* Accept: `Book` object
* Return: Created book object

#### **b. Update Book**

* Endpoint: `/books/{id}`
* HTTP Method: `@PutMapping`
* Access: `ADMIN`
* Function: Update book details
* Accept: `Book` object, `bookId` in path
* Return: Updated book object

#### **c. Delete Book**

* Endpoint: `/books/{id}`
* HTTP Method: `@DeleteMapping`
* Access: `ADMIN`
* Function: Delete a book
* Accept: `bookId` in path
* Return: HTTP status `No Content`

#### **d. View All Books**

* Endpoint: `/books`
* HTTP Method: `@GetMapping`
* Access: All authenticated users
* Return: List of all books with availability status

---

### **3. Borrowing Books (User Role)**

#### **Borrow Book**

* Endpoint: `/books/{bookId}/borrow`
* HTTP Method: `@PostMapping`
* Access: `USER`
* Function: Borrow an available book
* Accept: `bookId` in path
* Return: Success or error message
* Notes:

    * Only available books can be borrowed
    * If the book is already borrowed, return an error
    * Set `availability = false` when borrowed

#### **Return Book**

* Endpoint: `/books/{bookId}/return`
* HTTP Method: `@PostMapping`
* Access: `USER`
* Function: Return a borrowed book
* Accept: `bookId` in path
* Return: Success message
* Set `availability = true` when returned

---

### **4. User Account Management**

#### **View User Details**

* Endpoint: `/users/{userId}`
* HTTP Method: `@GetMapping`
* Access:

    * `ADMIN`: View any user's details
    * `USER`: View own details
* Return: User details

#### **Update User Details**

* Endpoint: `/users/{userId}`
* HTTP Method: `@PutMapping`
* Access:

    * `ADMIN`: Update any user
    * `USER`: Update own details
* Accept: `User` object, `userId` in path
* Return: Updated user object

---

## **Entities for the System**

---

### **1. Book Entity**

| Field          | Type    | Description                      |
| -------------- | ------- | -------------------------------- |
| `id`           | Long    | Auto-generated unique identifier |
| `title`        | String  | Title of the book                |
| `author`       | String  | Author of the book               |
| `description`  | String  | Brief description of the book    |
| `availability` | boolean | Book availability status         |

* Table name: `books`

---

### **2. User Entity**

| Field      | Type   | Description                      |
| ---------- | ------ | -------------------------------- |
| `id`       | Long   | Auto-generated unique identifier |
| `username` | String | Unique username                  |
| `password` | String | Encoded password                 |
| `role`     | String | Role: `"USER"` or `"ADMIN"`      |

* Table name: `users`

---

## **Security Key Points**

* Use **JWT tokens** for authentication and session management
* Encode passwords using **BCryptPasswordEncoder**
* Secure endpoints based on roles
* Implement `SecurityConfig.java` for Spring Security setup

    * Allow `/users/register` and `/users/login` without authentication
    * Use `hasAuthority("USER")` or `hasAuthority("ADMIN")`
* Handle unauthorized access and invalid operations properly

---

## **Exception Handling**

* Return error messages when:

    * A user tries to borrow a book that is already borrowed
    * Unauthorized access is attempted

---

## **JWT Authentication**

* Use JWT for stateless session management
* Return a token after successful login
* Validate token on every secured request

---

## **Test Cases**

| Feature             | Endpoint                      | Test Description                                 |
| ------------------- | ----------------------------- | ------------------------------------------------ |
| User Registration   | `POST /users/register`        | Register user and confirm DB entry               |
| Login               | `POST /users/login`           | Authenticate user and get valid JWT token        |
| Create Book         | `POST /books`                 | ADMIN creates book, check DB                     |
| Update Book         | `PUT /books/{bookId}`         | ADMIN updates book, verify update                |
| Delete Book         | `DELETE /books/{bookId}`      | ADMIN deletes book, verify removal               |
| View All Books      | `GET /books`                  | Fetch all books, verify availability             |
| Borrow Book         | `POST /books/{bookId}/borrow` | USER borrows book, update availability           |
| Return Book         | `POST /books/{bookId}/return` | USER returns book, set availability to true      |
| View User Details   | `GET /users/{userId}`         | View own or any user's details depending on role |
| Update User Details | `PUT /users/{userId}`         | USER or ADMIN updates user profile               |

---

Let me know if you want a project structure or starter code scaffolding as well.
