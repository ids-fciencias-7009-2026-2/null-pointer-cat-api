package com.nullpointercats.sys.adopta.controllers

import com.nullpointercats.sys.adopta.domain.User
import com.nullpointercats.sys.adopta.domain.toUser
import com.nullpointercats.sys.adopta.dto.request.LoginRequest
import com.nullpointercats.sys.adopta.dto.response.LogoutResponse
import com.nullpointercats.sys.adopta.dto.request.RegisterRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

/**
 * Controller for exposing the REST endopints related to the
 * management of users.
 */

@RestController
@RequestMapping("/users") // Base prefix
class UserController {
    val logger: Logger = LoggerFactory.getLogger(UserController::class.java)


    /**
     * Endpoint for retrieving the current user's information.
     *
     * URL: http://localhost:8080/users/me
     * Method: GET
     *
     * This endpoint simulates retrieving the authenticated user
     * from the system using fake data.
     *
     * @return ResponseEntity containing the user information
     *         and HTTP 200 (OK).
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
            0

        )

        logger.info("User found in system: $fakeUser")
        return ResponseEntity.ok(fakeUser)
    }

    /**
     * Endpoint for registering a new user
     *
     * URL:    http://localhost:8080/users/register
     * Method: POST
     *
     * @param registerUserRequest DTO that represents the body of the request.
     * @return ResponseEntity with the created user and the code HTTP 200 (OK).
     */
    @PostMapping("/register")
    fun addUser(
        @RequestBody registerUserRequest: RegisterRequest
    ): ResponseEntity<User> {

        val userToAdd = registerUserRequest.toUser()

        logger.info("User to add: $userToAdd")

        return ResponseEntity.ok(userToAdd)
    }


    /**
     * Endpoint for authenticating a user.
     *
     * URL:    http://localhost:8080/users/login
     * Method: POST
     * @param loginRequest DTO that represents the body of the request containing the user credentials.
     * @return ResponseEntity with:
     *          - HTTP 200(OK) if the login is successful.
     *          - HTTP 401(Unauthorized) if the credentials are invalid.
     */

    @PostMapping("/login")
    fun login(
        @RequestBody loginRequest: LoginRequest
    ): ResponseEntity<Any> {
        val fakeUser = User(
            "x-id",
            "x-name",
            "x-email",
            "test123",
            "x-fname",
            "x-lname",
            0
        )

        logger.info("Try to make login with:$loginRequest")


        return if (fakeUser.password == loginRequest.password) {
            logger.info("Login successful")

            ResponseEntity.ok(
                mapOf("message" to "Login successful")
            )

        } else {
            logger.error("Login failed")
            ResponseEntity.status(401).build()
        }

    }

    /**
     * Endpoint for user logout.
     *
     * URL:    http://localhost:8080/users/logout
     * Metodo: POST
     *
     * @return ResponseEntity with logout information (user id and logout time)
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
            0

        )

        val logoutResponse = LogoutResponse(
            fakeUser.id,
            LocalDateTime.now().toString()
        )

        logger.info("Logout successful! Hope to see you soon! ")

        return ResponseEntity.ok(logoutResponse)
    }

}