package com.nullpointercats.sys.adopta.controllers

import com.nullpointercats.sys.adopta.domain.User
import com.nullpointercats.sys.adopta.domain.toUser
import com.nullpointercats.sys.adopta.dto.request.LoginRequest
import com.nullpointercats.sys.adopta.dto.response.LogoutResponse
import com.nullpointercats.sys.adopta.dto.request.RegisterRequest
import com.nullpointercats.sys.adopta.dto.response.RegisterResponse
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
class UsuarioController {
    val logger: Logger = LoggerFactory.getLogger(UsuarioController::class.java)

    /**
     * Endpoint for registering a new user
     *
     * URL:    http://localhost:8080/users/register
     * Metodo: POST
     *
     * @param createUserRequest DTO that represents the body of the request.
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
}

    @PostMapping("/login")
    fun login(
        @RequestBody loginRequest: LoginRequest
    ): ResponseEntity<Any>{
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

        return if (fakeUser.password==loginRequest.password){
            logger.info("Login successful")

            ResponseEntity.ok(
                mapOf("message" to "Login successful")
            )

        } else {
            logger.error("Login failed")
            ResponseEntity.status(401).build()
        }

    }

