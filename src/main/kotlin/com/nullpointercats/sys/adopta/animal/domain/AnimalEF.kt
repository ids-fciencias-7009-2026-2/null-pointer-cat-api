package com.nullpointercats.sys.adopta.animal.domain
import com.nullpointercats.sys.adopta.animal.dto.request.AnimalRegisterRequest
import com.nullpointercats.sys.adopta.animal.dto.response.AnimalRegisterResponse
import com.nullpointercats.sys.adopta.animal.dto.response.AnimalSearchResponse
import com.nullpointercats.sys.adopta.animal.entities.AnimalEntity
import com.nullpointercats.sys.adopta.user.domain.*
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.String

/**
 * Converts an [AnimalRegisterRequest] DTO into an [Animal] domain model.
 */
fun AnimalRegisterRequest.toDomain(
    publisherDomain: User,
    breedDomain: Breed?
): Animal {
    return Animal(
        idAnimal = null,
        animalName = this.animalName,
        species = this.species,
        dateOfBirth = this.dateOfBirth,
        description = this.description,
        size = this.size,
        animalZipcode = this.animalZipcode,

        publisher = publisherDomain,
        breed = breedDomain,
        photos = this.photosURLs.map { url -> Photo(url = url) }
    )
}

fun Animal.toResponse() : AnimalRegisterResponse {
    return AnimalRegisterResponse(
        idAnimal = this.idAnimal,
        animalName = this.animalName,
        species = this.species,
        dateOfBirth = this.dateOfBirth,
        description = this.description,
        size = this.size,
        animalZipcode = this.animalZipcode,
        publishedAt = this.publishedAt
    )
}

/**
 * Converts an [AnimalEntity] into an [Animal] domain model.
 */
fun AnimalEntity.toDomain(): Animal {
    return Animal(
        idAnimal = this.idAnimal,
        animalName = this.animalName,
        species = this.species,
        dateOfBirth = this.dateOfBirth,
        description = this.description,
        size = this.size,
        animalZipcode = this.animalZipcode,
        publishedAt = this.publishedAt,

        publisher = this.user.toDomain(),
        breed = this.breed?.toDomain(),
        photos = this.photos.map { photoEntity -> photoEntity.toDomain() }

    )
}

fun Animal.toSearchResponse(): AnimalSearchResponse {
    return AnimalSearchResponse(
        idAnimal      = this.idAnimal,
        animalName    = this.animalName,
        species       = this.species,
        size          = this.size,
        description   = this.description,
        dateOfBirth   = this.dateOfBirth,
        animalZipcode = this.animalZipcode,
        publishedAt   = this.publishedAt,
        breedName     = this.breed?.breedName,
        photos        = this.photos.map { it.url }
    )
}

