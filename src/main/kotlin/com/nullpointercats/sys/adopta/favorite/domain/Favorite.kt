package com.nullpointercats.sys.adopta.favorite.domain

import java.time.LocalDateTime

data class Favorite(
    val userId: Int,
    val animalId: Int,
    val savedAt: LocalDateTime = LocalDateTime.now()
)
