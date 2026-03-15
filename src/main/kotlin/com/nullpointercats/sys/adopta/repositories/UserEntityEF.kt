package com.nullpointercats.sys.adopta.repositories

import  com.nullpointercats.sys.adopta.domain.User
import com.nullpointercats.sys.adopta.entities.UserEntity
import java.time.LocalDate

fun User.toUserEntity(): UserEntity {
    return UserEntity(
        id = null,
        firstname = this.firstname,
        lastname = this.lastname,
        birthdate = LocalDate.now(), // Not sure about it...
        gender = "No idea",
        email = this.email,
        password = this.password ?: " ",
        zipcode = this.zipcode,
        token = ""
    )
}