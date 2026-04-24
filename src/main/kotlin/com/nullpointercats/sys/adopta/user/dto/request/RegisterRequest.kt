package com.nullpointercats.sys.adopta.user.dto.request

/**
 * DTO used to receive user registration data
 * within the Adopta system.
 */
data class RegisterRequest(

    /**
     * Public display name of the user.
     */
    val username: String,

    /**
     * Email address of the user.
     */
    val email: String,

    /**
     * Raw password entered by the user.
     * Must be encrypted before persisting.
     */
    val password: String,

    /**
     * Given name of the user.
     */
    val firstname: String,

    /**
     * Family name of the user.
     */
    val lastname: String,

    /**
     * Postal code representing the user's location.
     */
    val zipcode: String
)