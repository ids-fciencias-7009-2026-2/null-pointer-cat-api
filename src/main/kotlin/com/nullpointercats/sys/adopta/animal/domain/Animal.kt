package com.nullpointercats.sys.adopta.animal.domain

import com.nullpointercats.sys.adopta.user.domain.User
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Represents an animal on adoption within Adopta system
 */
data class Animal (

    /**
     * Unique identifier of the animal.
     * */
    val idAnimal: Int ? = null,

    /**
     * [User] who registerd the adoption animal.
     */
    var publisher: User,

    /**
     * [Breed] of the animal
     */
    var breed: Breed ? = null,

    /**
     * Given name of the animal
     */
    var animalName : String,

    /**
     * Species of the animal. WE MAY USE A ENUMERATION.
     */
    var species : String,

    /**
     * The exact or estimated birthdate of the animal.
     */
    val dateOfBirth : LocalDate ? = null,

    /**
     * Brief description of the animal (background, temperament, personality).
     */
    val description: String ? = null,

    /**
     * Physical size of the animal.
     * */
    val size: String ? = null,

    /**
     * Zipcode of the current location of the animal.
     * */
    val animalZipcode: String,

    /**
     * The timestamp indicating when the animal's profile was published in the system
     * */
    val publishedAt: LocalDateTime? = LocalDateTime.now(),

    /**
     * A collection of [Photo] objects associated with the animal's profile.
     */
    val photos: List<Photo> = emptyList()

)