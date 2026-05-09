package com.nullpointercats.sys.adopta.favorite.repositories

import com.nullpointercats.sys.adopta.animal.entities.AnimalEntity
import com.nullpointercats.sys.adopta.favorite.entities.FavoriteEntity
import com.nullpointercats.sys.adopta.user.entities.UserEntity

fun toFavoriteEntity(
    userEntity: UserEntity,
    animalEntity: AnimalEntity
): FavoriteEntity {
    return FavoriteEntity(
        user = userEntity,
        animal = animalEntity
    )
}
