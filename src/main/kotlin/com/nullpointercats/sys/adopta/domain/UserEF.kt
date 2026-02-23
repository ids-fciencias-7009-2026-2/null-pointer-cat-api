package com.nullpointercats.sys.adopta.domain

import com.nullpointercats.sys.adopta.dto.request.RegisterRequest
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
        firstName = this.firstName,
        lastName = this.lastName,
        zipCode = this.zipCode
    )
}