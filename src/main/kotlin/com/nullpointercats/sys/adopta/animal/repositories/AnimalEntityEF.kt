package com.nullpointercats.sys.adopta.animal.repositories

import com.nullpointercats.sys.adopta.animal.domain.Animal
import com.nullpointercats.sys.adopta.animal.entities.*

import com.nullpointercats.sys.adopta.user.entities.UserEntity

/**
 * Converts an [Animal] domain model into an [AnimalEntity].
 */
fun Animal.toEntity(domainUser: UserEntity,
                    domainBreed: BreedEntity?) : AnimalEntity {

    val animalEntity = AnimalEntity(
        idAnimal = this.idAnimal ?: 0,
        animalName = this.animalName,
        species = this.species,
        dateOfBirth = this.dateOfBirth,
        description = this.description,
        size = this.size,
        animalZipcode = this.animalZipcode,
        breed = domainBreed,
        user = domainUser
    )

    this.photos.forEach { photoDomain ->
        val photoEntity = photoDomain.toEntity(animalEntity)
        animalEntity.photos.add(photoEntity)
    }

    return animalEntity
}