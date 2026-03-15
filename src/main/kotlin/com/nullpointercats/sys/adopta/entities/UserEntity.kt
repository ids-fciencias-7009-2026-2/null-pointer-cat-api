package com.nullpointercats.sys.adopta.entities

import jakarta.persistence.Entity
import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.time.LocalDate

@Entity
@Table(name = "adoption_user")
class UserEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    val id: Int? = null,

    @Column(name = "first_name")
    var firstname: String,

    @Column(name = "last_name")
    var lastname: String,

    @Column(name = "birth_date")
    var birthdate: LocalDate,

    @Column(name = "gender")
    var gender: String,

    @Column(name = "email")
    var email: String,

    @Column(name = "zipcode")
    var zipcode: String,

    @Column(name = "register_date")
    var registerDate: LocalDateTime = LocalDateTime.now(),

    @Column(name = "user_password")
    var password: String,

    @Column(name = "token")
    var token: String? = null
)