package com.nullpointercats.sys.adopta.dto.response

/**
 * DTO used to send logout confirmation information back to
 * the client after a successful operation.
 */

data class LogoutResponse(
    /**
     * Confirmation message of the logout operation.
     */
    val message: String = "Logged out successfully",
    /**
     * Date and time when the logout operation occurred.
     */
    val logoutDateTime: String
)
