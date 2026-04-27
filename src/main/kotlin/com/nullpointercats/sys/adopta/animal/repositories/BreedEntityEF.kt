package com.nullpointercats.sys.adopta.animal.repositories

import com.nullpointercats.sys.adopta.animal.domain.Breed
import com.nullpointercats.sys.adopta.animal.entities.BreedEntity

/**
 * Converts a [Breed] domain model to an [BreedEntity].
 */
fun Breed.toEntity() : BreedEntity {
    return BreedEntity(
        idBreed = this.idBreed ?: 0,
        breedName = this.breedName,
        origin = this.origin,
        temperament = this.temperament,
        lifeSpan = this.lifeSpan,
    )
}