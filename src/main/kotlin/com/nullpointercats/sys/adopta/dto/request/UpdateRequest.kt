package com.nullpointercats.sys.adopta.dto.request

/**
 * DTO used to receive user update data
 * within the Adopta system.
 */
data class UpdateRequest(

    /**
     * New public display name of the user.
     */
    val username: String,

    /**
     * New given name of the user.
     */
    val firstname: String,

    /**
     * New family name of the user.
     */
    val lastname: String,

    /**
     * New postal code representing the user's location.
     */
    val zipcode: String
)
