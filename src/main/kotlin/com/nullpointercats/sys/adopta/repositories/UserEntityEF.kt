package com.nullpointercats.sys.adopta.repositories

import  com.nullpointercats.sys.adopta.domain.User
import com.nullpointercats.sys.adopta.entities.UserEntity
import java.time.LocalDate

/** Extension funtion to map a User domain to a UserEntity.
 * @return UserEntity A new instance of UserEntity
 * */
fun User.toUserEntity(): UserEntity {
    return UserEntity(
        id = null,
        firstname = this.firstname,
        username = this.username,
        lastname = this.lastname,
        birthdate = LocalDate.now(), // Not sure about it...
        gender = "No idea",
        email = this.email,
        password = this.password ?: " ",
        zipcode = this.zipcode,
        token = ""
    )
}