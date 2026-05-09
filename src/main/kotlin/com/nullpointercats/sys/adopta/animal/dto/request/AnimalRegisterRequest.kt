package com.nullpointercats.sys.adopta.animal.dto.request

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * DTO representing the incoming rquest from a client
 * when attempting to register a new animal in the system.
 */
data class AnimalRegisterRequest (
    var animalName : String,
    var species : String,
    val dateOfBirth : LocalDate ? = null,
    val description: String ? = null,
    val size: String ? = null,
    val animalZipcode: String,
    val publishedAt: LocalDateTime? = LocalDateTime.now(),

    val breedId: Int? = null,
    val photos: List<PhotoRegisterRequest> = emptyList()
)

data class PhotoRegisterRequest (
    var url : String,
    var width : Int ?= null,
    var height : Int ?= null,
)