package com.nullpointercats.sys.adopta.user.dto.response

/**
 * DTO used to send user registration confirmation
 * back to the client after a successful operation.
 */
data class RegisterResponse(

    /**
     * Unique identifier of the newly created user.
     */
    val userId: String,

    /**
     * Public display name of the user.
     */
    val username: String,

    /**
     * Email address of the user.
     */
    val email: String,

    /**
     * Date and time when the registration occurred.
     */
    val registrationDateTime: String
)