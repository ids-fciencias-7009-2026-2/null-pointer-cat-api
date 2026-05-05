package com.nullpointercats.sys.adopta.animal.dto.response

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * DTO representing the response sent back to the client
 * after a search query for animals available for adoption.
 */
data class AnimalSearchResponse(
    val idAnimal: Int?,
    val animalName: String,
    val species: String,
    val size: String?,
    val description: String?,
    val dateOfBirth: LocalDate?,
    val animalZipcode: String,
    val publishedAt: LocalDateTime?,
    val breedName: String?,
    val photos: List<String>
)