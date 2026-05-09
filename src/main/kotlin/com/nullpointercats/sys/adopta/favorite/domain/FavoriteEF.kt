package com.nullpointercats.sys.adopta.favorite.domain

import com.nullpointercats.sys.adopta.favorite.entities.FavoriteEntity

fun FavoriteEntity.toDomain(): Favorite {
    return Favorite(
        userId = this.user.id!!,
        animalId = this.animal.idAnimal,
        savedAt = this.savedAt
    )
}
