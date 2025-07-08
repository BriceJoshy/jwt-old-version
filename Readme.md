# D21\_S2\_A1: Spring Boot-based Library Management System with Spring Security

Develop a Spring Boot-based web application for a Library Management System with basic Spring Security features to manage book records, users, and borrowing operations. The system should enable users to register, borrow books, view available books, and manage user accounts. Additionally, there should be role-based access control with roles such as **"USER"** (who can borrow books) and **"ADMIN"** (who can manage books and users).

---

## Functional Requirements

### 1. User Registration and Authentication

#### User Registration

* Endpoint: `POST /users/register`
* Accepts: `User` object in request body
* Returns: Success message in response
* Roles assigned during registration: **USER** or **ADMIN**
* Passwords must be encoded using `BCryptPasswordEncoder`

#### User Login

* Endpoint: `POST /users/login`
* Accepts: `AuthRequest` object in request body
* Returns: `AuthResponse` object containing JWT token

> `AuthRequest` and `AuthResponse` are custom classes located in `/src/main/java/com/wecp/library_management_system_jwt/dto/`

#### Role-Based Access Control

* **USER**: Can borrow books and view book details
* **ADMIN**: Can add, update, delete books, and view all users

---

### 2. Book Management (Admin Role)

#### Create Book

* Endpoint: `POST /books`
* Accepts: `Book` object in request body
* Returns: Created `Book` object

#### Update Book

* Endpoint: `PUT /books/{id}`
* Accepts: `Book` object in request body and `bookId` in path
* Returns: Updated `Book` object

#### Delete Book

* Endpoint: `DELETE /books/{id}`
* Accepts: `bookId` in path
* Returns: HTTP status `204 No Content`

#### View All Books

* Endpoint: `GET /books`
* Returns: List of all books with availability status
* Access: All authenticated users

---

### 3. Borrowing Books (User Role)

#### Borrow Book

* Endpoint: `POST /books/{bookId}/borrow`
* Accepts: `bookId` in path
* Returns: Success or error message
* Constraints: Only available books can be borrowed (availability = false when borrowed)

#### Return Book

* Endpoint: `POST /books/{bookId}/return`
* Accepts: `bookId` in path
* Returns: Success message
* Effect: Sets book availability to `true`

---

### 4. User Account Management

#### View User Details

* Endpoint: `GET /users/{userId}`
* Access:

    * Regular users: Can view their own details
    * Admins: Can view any user's details

#### Update User Details

* Endpoint: `PUT /users/{userId}`
* Accepts: `User` object in request body and `userId` in path
* Returns: Updated `User` object
* Access:

    * Users and Admins can update account details

---

## Entities for the System

### Book Entity

* `id` (Long) – Unique identifier (auto-generated)
* `title` (String) – Title of the book
* `author` (String) – Author of the book
* `description` (String) – A brief description of the book
* `availability` (boolean) – Whether the book is available

**Table Name:** `books`

### User Entity

* `id` (Long) – Unique identifier (auto-generated)
* `username` (String) – Unique username
* `password` (String) – Encoded password
* `role` (String) – "USER" or "ADMIN"

**Table Name:** `users`

---

## Security Key Points

* Use JWT tokens for authentication and session management
* Passwords must be encoded using `BCryptPasswordEncoder`
* Use `SecurityConfig.java` for configuring Spring Security
* Authorities should be configured using `hasAuthority("USER")` or `hasAuthority("ADMIN")`
* Open access:

    * `/users/register`
    * `/users/login`
* Secure access:

    * Book operations and user management by roles

### Exception Handling

* Borrowing unavailable books → error message
* Unauthorized access → error message

---

## Test Cases

1. **User Registration:**

    * `POST /users/register`
    * Validate new user saved to DB

2. **Login:**

    * `POST /users/login`
    * Validate JWT token is returned

3. **Create Book (ADMIN):**

    * `POST /books`
    * Validate new book in DB

4. **Update Book (ADMIN):**

    * `PUT /books/{bookId}`
    * Validate updated book in DB

5. **Delete Book (ADMIN):**

    * `DELETE /books/{bookId}`
    * Validate book removed

6. **View All Books:**

    * `GET /books`
    * Validate returned list and availability

7. **Borrow Book (USER):**

    * `POST /books/{bookId}/borrow`
    * Validate availability = false

8. **Return Book (USER):**

    * `POST /books/{bookId}/return`
    * Validate availability = true

9. **View User Details:**

    * `GET /users/{userId}`
    * Validate role-based access

---
