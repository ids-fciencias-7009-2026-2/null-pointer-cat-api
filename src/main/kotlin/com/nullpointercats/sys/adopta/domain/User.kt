package com.nullpointercats.sys.adopta.domain

/**
 * Represents a user within Adopta system
 */
data class User(

    /**
    * Unique identifier of the user.
    * */
    val id        : String,

    /**
    * Public display name of the user.
    * */
    var username  : String,

    /**
    * Email address of the user.
    *
    * */
    var email     : String,

    /**
    * Password of the user. Must be encrypted.
    * */
    var password  : String,

    /**
    * Given name of the user.
    * */
    var firstName : String,

    /**
    * Family name of the user.
    * */
    var lastName  : String,

    /**
    * Postal code representing the user's location.
    * */
    var zipCode   : String
)