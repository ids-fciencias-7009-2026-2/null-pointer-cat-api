package com.nullpointercats.sys.adopta.animal.dto.response

import java.time.LocalDate
import java.time.LocalDateTime

data class AnimalRegisterResponse (
    val idAnimal: Int,
    val animalName: String,
    val species: String,
    val description: String?,
    val dateOfBirth: LocalDate?,
    val size: String?,
    val animalZipcode: String,
    val publishedAt: LocalDateTime?,

    val breedName: String?,
    val publisherName: String,
    val photoURLs: List<String>
)