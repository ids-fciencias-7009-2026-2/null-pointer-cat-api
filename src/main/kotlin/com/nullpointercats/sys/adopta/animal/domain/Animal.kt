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

    var publisher: User,

    var breed: Breed ? = null,

    var animalName : String,

    var species : String,

    val dateOfBirth : LocalDate ? = null,

    val description: String ? = null,

    val size: String ? = null,

    val animalZipcode: String,

    val publishedAt: LocalDateTime? = LocalDateTime.now(),

    val photos: List<Photo> = emptyList()

)