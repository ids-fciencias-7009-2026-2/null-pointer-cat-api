package com.nullpointercats.sys.adopta.entities

import  com.nullpointercats.sys.adopta.domain.User
import java.time.LocalDate

fun User.toUserEntity(): UserEntity {
    return UserEntity(
        firstName = this.firstName,
        lastName = this.lastName,
        birthDate = LocalDate.now(), // Not sure about it...
        gender =  "No idea",
        email = this.email,
        password = this.password ?: " ",
        zipcode = this.zipCode,
        token = ""
    )
}