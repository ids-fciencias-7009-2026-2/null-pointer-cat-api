package com.nullpointercats.sys.adopta.favorite.dto.response

import java.time.LocalDateTime

data class FavoriteResponse(
    val userId: Int,
    val animalId: Int,
    val savedAt: LocalDateTime,
    val message: String = "¡Tu interés ha sido registrado!"
)
