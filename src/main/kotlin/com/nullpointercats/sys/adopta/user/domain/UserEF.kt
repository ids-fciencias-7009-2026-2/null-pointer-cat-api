package com.nullpointercats.sys.adopta.user.domain

import com.nullpointercats.sys.adopta.user.dto.request.RegisterRequest
import com.nullpointercats.sys.adopta.user.entities.UserEntity
import java.util.UUID

/**
 * Extension Function that converts a CreateUserRequest type DTO
 * into a User domain object.
 */

fun RegisterRequest.toUser(): User {
    return User(
        id = UUID.randomUUID().toString(),
        username = this.username,
        email = this.email,
        password = this.password,
        firstname = this.firstname,
        lastname = this.lastname,
        zipcode = this.zipcode
    )
}

fun UserEntity.toUser(): User {
    return User(
        id = this.id.toString(),
        username = this.username,
        firstname = this.firstname,
        lastname = this.lastname,
        email = this.email,
        token = this.token,
        zipcode = this.zipcode)
}
