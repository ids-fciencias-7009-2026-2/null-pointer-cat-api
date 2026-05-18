package com.nullpointercats.sys.adopta.animal.dto.response

import java.time.LocalDate
import java.time.LocalDateTime

data class AnimalResponse(
    val idAnimal: Int,
    val animalName: String,
    val species: String,
    val size: String?,
    val dateOfBirth: LocalDate?,
    val animalZipcode: String,
    val breedName: String?,
    val animalDescription: String?,
    val photos: List<String>,
    val createdAt: LocalDateTime,

    val publisherUsername: String,
    val publisherFirstname: String,
    val publisherLastname: String,
    val publisherId: String
)
