package com.nullpointercats.sys.adopta.animal.domain

import com.nullpointercats.sys.adopta.animal.entities.BreedEntity

fun BreedEntity.toDomain() : Breed {
    return Breed(
        idBreed = this.idBreed,
        breedName = this.breedName,
        origin = this.origin,
        temperament = this.temperament,
        lifeSpan = this.lifeSpan,
    )
}