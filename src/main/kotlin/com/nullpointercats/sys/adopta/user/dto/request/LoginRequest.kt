package com.nullpointercats.sys.adopta.user.dto.request

/**
 * DTO used to receive user authentication credentials
 * within the Adopta system.
 */
data class LoginRequest(

    /**
     * Email address of the user attempting authentication.
     */
    val email: String,
    /**
     * Password entered by the User.
     */
    val password: String
)

