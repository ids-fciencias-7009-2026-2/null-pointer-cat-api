package com.nullpointercats.sys.adopta.domain

data class User(
    val id        : String,
    var username  : String,
    var email     : String,
    var password  : String,
    var firstName : String,
    var lastName  : String,
    var zipCode   : Int
)