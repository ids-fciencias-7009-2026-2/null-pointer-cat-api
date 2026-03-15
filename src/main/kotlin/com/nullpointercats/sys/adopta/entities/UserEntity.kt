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
@Table(name = "\"AdoptionUser\"")
class UserEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdUser")
    val id: Int? = null,

    @Column(name = "FirstName")
    var firstname : String,

    @Column(name = "LastName")
    var lastname : String,

    @Column(name = "BirthDate")
    var birthdate : LocalDate,

    @Column(name = "Gender")
    var gender : String,

    @Column(name = "email")
    var email : String,

    @Column(name = "zipcode")
    var zipcode : String,

    @Column(name = "registerDate")
    var registerDate: LocalDateTime = LocalDateTime.now(),

    @Column(name = "UserPassword")
    var password: String,

    @Column(name = "token")
    var token: String? = null,


)