# null-pointer-cat-api

REST API built with Spring Boot and Kotlin for AdoptaPet, a platform for adopting and listing dogs for adoption.

---

## Team

Null Pointer Cats

---

## Tech Stack

- Kotlin
- Spring Boot
- Maven
- JDK 21

---

## How to run the project

Clone the repository:

```bash
git clone <repository-url>
cd null-pointer-cat-api
```

Run the server:

```bash
mvn spring-boot:run
```

The server will start at `http://localhost:8080`

---
## Postgres Database

### Configuration

1. Connect to PostgreSQL with your user:
```bash
   psql -U postgres
```

2. Create the databse:
```bash
   CREATE DATABASE adopta_db;
```
3. Connect to the database:
```bash
   \c adopta_db
```

4. Execute the schema:
```bash
   psql -U postgres -d adopta_db -f schema.sql
```

### To reset the database
```bash
psql -U postgres -c "DROP DATABASE IF EXISTS adopta_db;"
psql -U postgres -c "CREATE DATABASE adopta_db;"
psql -U postgres -d adopta_db -f schema.sql
```

## Project Structure

```
src/
  main/
    kotlin/com/nullpointercats/sys/adopta/
      controllers/
        UserController.kt       # Handles all HTTP requests for users
      domain/
        User.kt                 # Domain model representing a user
        UserEF.kt               # Extension function to convert DTO to domain
      dto/
        request/
          LoginRequest.kt       # DTO for login credentials
          RegisterRequest.kt    # DTO for user registration data
          UpdateRequest.kt      # DTO for user update data
        response/
          LogoutResponse.kt     # DTO for logout confirmation
          RegisterResponse.kt   # DTO for registration confirmation
postman/
  practica1-nullpointercats.postman_collection.json
database/
  diagrama-er.pdf
```

---

## Endpoints

All endpoints are prefixed with `/users`.

| Method | Endpoint        | Description                            | HTTP Code    |
|--------|-----------------|----------------------------------------|--------------|
| GET    | /users/me       | Returns the current logged-in user     | 200 OK       |
| POST   | /users/register | Registers a new user                   | 200 OK       |
| POST   | /users/login    | Authenticates a user                   | 200 OK / 401 |
| POST   | /users/logout   | Logs out the current user              | 200 OK       |
| PUT    | /users          | Updates the current user's information | 200 OK       |

---

## API Behavior

### GET /users/me
Returns the information of the currently logged-in user. No request body needed.

### POST /users/register
Creates a new user account. Expects a JSON body with username, email, password, firstName, lastName and zipCode. Returns the created user with a generated UUID as id.

### POST /users/login
Authenticates a user with email and password. Returns 200 OK with a success message if the credentials are correct, or 401 Unauthorized if they are not.

### POST /users/logout
Ends the current user session. No request body needed. Returns the user id and the exact date and time of logout.

### PUT /users
Updates the current user's information. Expects a JSON body with username, firstName, lastName and zipCode. The email and id remain unchanged.

---

## Notes

- This practice does not use a real database. All responses are simulated with fake data.
- No real authentication or security is implemented at this stage.
- The Postman collection is available in the `/postman` folder.
- The Entity-Relationship diagram is available in the `/database` folder.

---

## Postman Collection

Import the file `postman/practica1-nullpointercats.postman_collection.json` into Postman to test all endpoints.

---

## Demo Video

[Watch the Postman testing demo here](https://drive.google.com/file/d/1ClgdWnU3tgZUISiqNMLhTAPFYLBTuzZF/view?usp=sharing)
