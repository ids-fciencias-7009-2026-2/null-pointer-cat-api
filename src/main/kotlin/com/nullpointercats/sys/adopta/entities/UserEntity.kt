package com.nullpointercats.sys.adopta.entities

import jakarta.persistence.Entity
import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.time.LocalDate

/**
 * UserEntity is a persistence entity that represents a user un the system.
 * This class maps to the apoption_user table and stores personal information.
 * */
@Entity
@Table(name = "adoption_user")
class UserEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    val id: Int? = null,

    @Column(name = "username")
    var username: String,

    @Column(name = "first_name")
    var firstname: String,

    @Column(name = "last_name")
    var lastname: String,

    @Column(name = "birth_date")
    var birthdate: LocalDate,

    @Column(name = "gender")
    var gender: String,

    @Column(name = "email", unique = true, nullable = false )
    var email: String,

    @Column(name = "zipcode")
    var zipcode: String,

    @Column(name = "register_date")
    var registerDate: LocalDateTime = LocalDateTime.now(),

    /** Hashed password.*/
    @Column(name = "user_password")
    var password: String,

    /** Authetification token. Optional*/
    @Column(name = "token")
    var token: String? = null
)