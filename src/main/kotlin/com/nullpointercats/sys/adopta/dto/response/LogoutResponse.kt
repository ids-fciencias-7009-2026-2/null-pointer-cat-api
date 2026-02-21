package com.nullpointercats.sys.adopta.dto.response

/**
 * DTO used to send logout confirmation information back to
 * the client after a successful operation.
 */

data class LogoutResponse(
    /**
     * Unique identifier of the user who logged out.
     */
    val userId: String,
    /**
     * Date and time when the logout operation occurred.
     */
    val logoutDateTime: String
)
