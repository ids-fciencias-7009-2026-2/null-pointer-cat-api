package com.nullpointercats.sys.adopta.animal.dto.response

data class AnimalLocationResponse(
    val animalId: Int,
    val animalName: String,
    val species: String,
    val animalZipcode: String
)