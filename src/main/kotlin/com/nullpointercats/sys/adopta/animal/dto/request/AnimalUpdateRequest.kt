package com.nullpointercats.sys.adopta.animal.dto.request

import java.time.LocalDate

data class AnimalUpdateRequest(
    val animalName: String? = null,
    val species: String? = null,
    val dateOfBirth: LocalDate? = null,
    val description: String? = null,
    val size: String? = null,
    val animalZipcode: String? = null,
    val breedId: Int? = null,
)