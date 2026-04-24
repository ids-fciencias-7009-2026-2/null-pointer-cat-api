package com.nullpointercats.sys.adopta.animal.entities

import jakarta.persistence.*
/**
 * BreedEntity is a persistence entity that represents an animal breed in the system.
 * This class maps to the apoption_breed table.
 * */
@Entity
@Table(name = "breed")
data class BreedEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_breed")
    var idBreed : Int = 0,

    @Column(name = "breed_name", nullable = false)
    var breedName : String,

    @Column(name = "origin")
    var origin : String ? = null,

    @Column(name = "temperament")
    var temperament : String ? = null,

    @Column(name = "life_span")
    var lifeSpan : String ? = null,

)