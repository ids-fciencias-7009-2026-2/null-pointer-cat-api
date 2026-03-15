package com.nullpointercats.sys.adopta.controllers

import com.nullpointercats.sys.adopta.domain.User
import com.nullpointercats.sys.adopta.domain.toUser

import com.nullpointercats.sys.adopta.dto.request.LoginRequest
import com.nullpointercats.sys.adopta.dto.response.LoginResponse
import com.nullpointercats.sys.adopta.dto.request.RegisterRequest
import com.nullpointercats.sys.adopta.dto.request.UpdateRequest
import com.nullpointercats.sys.adopta.dto.response.LogoutResponse
import com.nullpointercats.sys.adopta.services.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.MessageDigest
import java.time.LocalDateTime
import java.util.UUID

/**
 * Controller for exposing the REST endpoints related to the
 * management of users.
 */
@RestController
@RequestMapping("/users")
class UserController {
    val logger: Logger = LoggerFactory.getLogger(UserController::class.java)

    @Autowired
    lateinit var userService: UserService
    val activeTokens = mutableSetOf<String>()

    /**
     * Endpoint for retrieving the current user's information.
     *
     * URL: http://localhost:8080/users/me
     * Method: GET
     */
    @GetMapping("/me")
    fun retrieveUser(): ResponseEntity<User> {
        val fakeUser = User(
            "x-id",
            "x-username",
            "x-email@gmail.com",
            "test123",
            "x-fname",
            "x-lname",
            "0"
        )
        logger.info("User found in system: $fakeUser")
        return ResponseEntity.ok(fakeUser)
    }

    /**
     * Endpoint for registering a new user.
     *
     * URL:    http://localhost:8080/users/register
     * Method: POST
     */
    @PostMapping("/register")
    fun addUser(
        @RequestBody registerUserRequest: RegisterRequest
    ): ResponseEntity<User> {
        val userToAdd = registerUserRequest.toUser()
        val password = hashPassword(registerUserRequest.password)
        userToAdd.password = password
        userService.addNewUser(userToAdd)
        logger.info("User to add: $userToAdd")
        return ResponseEntity.ok(userToAdd)
    }

    /**
     * Endpoint for authenticating a user.
     *
     * URL:    http://localhost:8080/users/login
     * Method: POST
     */
    @PostMapping("/login")
    fun login(
        @RequestBody loginRequest: LoginRequest
        ): ResponseEntity<Any> {
            
            val passwordHash = hashPassword(loginRequest.password)
            logger.info("password from request: $passwordHash")
            
            val userFound = userService.login(loginRequest.email, passwordHash)
            logger.info("try make login with: $loginRequest")
            logger.info("user password: ${userFound?.password}")
            
            return if (userFound != null) {
                ResponseEntity.ok(LoginResponse(userFound.token.orEmpty()))
            } else {
                ResponseEntity.status(401).build()
            }
        }

    /**
     * Endpoint for user logout.
     *
     * URL:    http://localhost:8080/users/logout
     * Method: POST
     */
    @PostMapping("/logout")
    fun logout(): ResponseEntity<Any> {
        val fakeUser = User(
            "x-id",
            "x-username",
            "x-email@gmail.com",
            "test123",
            "x-fname",
            "x-lname",
            "0"
        )
        val logoutResponse = LogoutResponse(
            fakeUser.id,
            LocalDateTime.now().toString()
        )
        logger.info("Logout successful! Hope to see you soon!")
        return ResponseEntity.ok(logoutResponse)
    }

    /**
     * Endpoint for updating the current user's information.
     *
     * URL:    http://localhost:8080/users
     * Method: PUT
     *
     * @param updateRequest DTO containing the fields to update.
     * @return ResponseEntity with the updated user and HTTP 200 (OK).
     */
    @PutMapping
    fun updateUser(
        @RequestBody updateRequest: UpdateRequest
    ): ResponseEntity<User> {
        // Simulate fetching the existing user (fake data)
        val existingUser = User(
            id        = "x-id",
            username  = "x-username",
            email     = "x-email@gmail.com",
            password  = "test123",
            firstname = "x-fname",
            lastname  = "x-lname",
            zipcode   = "0"
        )

        // Apply the updates
        val updatedUser = existingUser.copy(
            username  = updateRequest.username,
            firstname = updateRequest.firstname,
            lastname  = updateRequest.lastname,
            zipcode   = updateRequest.zipcode
        )

        logger.info("User updated: $updatedUser")
        return ResponseEntity.ok(updatedUser)
    }

    fun hashPassword(password: String): String {
        val bytes = MessageDigest
            .getInstance("SHA-256")
            .digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun tokenGenerator(): String {
        val token = UUID.randomUUID().toString()
        return token
    }
}