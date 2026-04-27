package com.nullpointercats.sys.adopta.user.controllers

import com.nullpointercats.sys.adopta.user.domain.User
import com.nullpointercats.sys.adopta.user.domain.toDomain

import com.nullpointercats.sys.adopta.user.dto.request.LoginRequest
import com.nullpointercats.sys.adopta.user.dto.response.LoginResponse
import com.nullpointercats.sys.adopta.user.dto.request.RegisterRequest
import com.nullpointercats.sys.adopta.user.dto.request.UpdateRequest
import com.nullpointercats.sys.adopta.user.dto.response.LogoutResponse
import com.nullpointercats.sys.adopta.user.services.UserService
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
    fun retrieveUser(
        @RequestAttribute("authenticatedUser") user: User //
    ): ResponseEntity<User> {

        logger.info("[users/me] [SUCCESS] User found in service: $user")
        return ResponseEntity.ok(user)
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

        logger.info("[users/register] [ATTEMPT] Attempting new user registration with mail ${registerUserRequest.email}")
        val userToAdd = registerUserRequest.toDomain()
        val password = hashPassword(registerUserRequest.password)
        userToAdd.password = password

        val userAdded = userService.addNewUser(userToAdd)
        if(userAdded == null) {
            logger.warn("[users/register] [FAILED] The email ${registerUserRequest.email} has been registered before.")
            return ResponseEntity.status(409).build()
        }

        logger.info("[users/register] [SUCCESS] User registered successfully with email ${userAdded.email} ")
        return ResponseEntity.ok(userAdded)
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
            logger.info("[users/login] [ATTEMPT] Attempting login for email ${loginRequest.email}")

            val passwordHash = hashPassword(loginRequest.password)
            val userFound = userService.login(loginRequest.email, passwordHash)
            
            if (userFound == null) {
                logger.info("[users/login] [FAILED] Invalid credentials for email ${loginRequest.email}")
                return ResponseEntity.status(401).build()
            }

            logger.info("[users/login] [SUCCESS] Successfully login for user ${loginRequest.email} ")
            return ResponseEntity.ok(LoginResponse(userFound.token.orEmpty()))
        }

    /**
     * Endpoint for user logout.
     *
     * URL:    http://localhost:8080/users/logout
     * Method: POST
     */
    @PostMapping("/logout")
    fun logout(
        @RequestHeader("Authorization") authHeader: String
    ): ResponseEntity<Any> {
        val cleanToken = authHeader.removePrefix("Bearer ").trim()
        logger.info("[users/logout] [ATTEMPT] From token [${cleanToken.take(10)}]")

        val success = userService.logout(cleanToken)

        if (!success) {
            logger.warn("[users/logout] [FAILED] Invalid token or already log out.")
            return ResponseEntity.status(401).build()
        }

        logger.info("[users/logout] [SUCCESS] Successfully log out.")
        return ResponseEntity.ok(LogoutResponse(logoutDateTime = LocalDateTime.now().toString()))
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
        @RequestAttribute("authenticatedUser") userFound: User,
        @RequestBody updateRequest: UpdateRequest
    ): ResponseEntity<User> {

        val updatedUser =  userFound.copy(
            username  = updateRequest.username  ?: userFound.username,
            firstname = updateRequest.firstname ?: userFound.firstname,
            lastname  = updateRequest.lastname  ?: userFound.lastname,
            zipcode   = updateRequest.zipcode   ?: userFound.zipcode
        )

        val savedUser = userService.updateUser(updatedUser)

        if (savedUser == null) {
            logger.warn("[UPDATE /users] [FAILED] Failed to update user.")
            return ResponseEntity.status(500).build()
        }

        logger.info("[UPDATE /users] [SUCCESS] User found successfully updated. ${savedUser}")
        return ResponseEntity.ok(savedUser)

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