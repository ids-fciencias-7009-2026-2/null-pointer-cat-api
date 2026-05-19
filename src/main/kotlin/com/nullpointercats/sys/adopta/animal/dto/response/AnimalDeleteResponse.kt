package com.nullpointercats.sys.adopta.animal.dto.response

import java.time.LocalDateTime

/**
 * DTO representing the response sent back to the client
 * after an animal post is successfully deleted.
 */
data class AnimalDeleteResponse(
    val idAnimal: Int?,
    val animalName: String,
    val deletedAt: LocalDateTime,
    val message: String,
)
