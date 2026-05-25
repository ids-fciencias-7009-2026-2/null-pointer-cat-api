package com.nullpointercats.sys.adopta.animal.domain
import com.nullpointercats.sys.adopta.animal.dto.request.AnimalRegisterRequest
import com.nullpointercats.sys.adopta.animal.dto.response.AnimalRegisterResponse
import com.nullpointercats.sys.adopta.animal.dto.response.AnimalResponse
import com.nullpointercats.sys.adopta.animal.dto.response.AnimalSearchResponse
import com.nullpointercats.sys.adopta.animal.entities.AnimalEntity
import com.nullpointercats.sys.adopta.animal.dto.response.AnimalUpdateResponse
import com.nullpointercats.sys.adopta.animal.dto.response.AnimalDeleteResponse
import com.nullpointercats.sys.adopta.animal.dto.response.AnimalLocationResponse
import com.nullpointercats.sys.adopta.user.domain.*
import java.time.LocalDateTime

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
        photos = this.photos.map { photoReq ->
            Photo(url = photoReq.url,
                  width = photoReq.width,
                  height = photoReq.height) }
    )
}

fun Animal.toRegisterResponse() : AnimalRegisterResponse {
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
    val localBreed = this.breed
    val generalInfo = if (localBreed != null) {
        "Breed originating from ${localBreed.origin ?: "Unspecified origin"}. Has an average life expectancy of ${localBreed.lifeSpan ?: "N/A"}."
    } else null

    val careRecommendations = localBreed?.temperament?.let { temperamentString ->
        val temp = temperamentString.lowercase()
        when {
            temp.contains("active") || temp.contains("energetic") || temp.contains("playful") ->
                "Requires high doses of daily exercise, long walks, and interactive toys to channel their energy."
            temp.contains("docile") || temp.contains("calm") || temp.contains("quiet") ->
                "Adapts very well to small spaces or apartments. Requires moderate walks and a calm environment."
            else -> "Requires a balanced diet according to their size, and periodic veterinary visits."
        }
    } ?: if (localBreed != null) {
        "Requires proper nutrition, brushing, and regular veterinary checkups."
    } else null

    val characteristics = localBreed?.temperament?.let {
        "Some distinct behavioral characteristics are: $it."
    }
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
        breedGeneralInfo = generalInfo,
        breedCareRecommendations = careRecommendations,
        breedRelevantCharacteristics = characteristics,
        photos        = this.photos.map { it.url },
        publisherUsername  = this.publisher.username,
        publisherFirstname = this.publisher.firstname,
        publisherLastname  = this.publisher.lastname,
    )
}

fun Animal.toUpdateResponse(): AnimalUpdateResponse {
    return AnimalUpdateResponse(
        idAnimal      = this.idAnimal,
        animalName    = this.animalName,
        species       = this.species,
        description   = this.description,
        dateOfBirth   = this.dateOfBirth,
        size          = this.size,
        animalZipcode = this.animalZipcode,
        publishedAt   = this.publishedAt
    )
}

fun Animal.toResponse(): AnimalResponse {
    val localBreed = this.breed
    val generalInfo = if (localBreed != null) {
        "Breed originating from ${localBreed.origin ?: "Unspecified origin"}. Has an average life expectancy of ${localBreed.lifeSpan ?: "N/A"}."
    } else null

    val careRecommendations = localBreed?.temperament?.let { temperamentString ->
        val temp = temperamentString.lowercase()
        when {
            temp.contains("active") || temp.contains("energetic") || temp.contains("playful") ->
                "Requires high doses of daily exercise, long walks, and interactive toys to channel their energy."
            temp.contains("docile") || temp.contains("calm") || temp.contains("quiet") ->
                "Adapts very well to small spaces or apartments. Requires moderate walks and a calm environment."
            else -> "Requires a balanced diet according to their size, and periodic veterinary visits."
        }
    } ?: if (localBreed != null) {
        "Requires proper nutrition, brushing, and regular veterinary checkups."
    } else null

    val characteristics = localBreed?.temperament?.let {
        "Some distinct behavioral characteristics are: $it."
    }
    return AnimalResponse(
        idAnimal = this.idAnimal ?:0,
        animalName = this.animalName,
        species       = this.species,
        dateOfBirth   = this.dateOfBirth,
        size           = this.size,
        animalZipcode = this.animalZipcode,
        breedName = this.breed?.breedName,
        breedGeneralInfo = generalInfo,
        breedCareRecommendations = careRecommendations,
        breedRelevantCharacteristics = characteristics,
        photos        = this.photos.map { it.url },
        createdAt = this.publishedAt ?: LocalDateTime.now(),
        description = this.description,

        publisherUsername = this.publisher.username,
        publisherFirstname = this.publisher.firstname,
        publisherLastname = this.publisher.lastname,
        publisherId       = this.publisher.id.toInt(),
    )
}

/**
 * Builds the response returned after an animal post is deleted.
 */
fun Animal.toDeleteResponse(): AnimalDeleteResponse {
    return AnimalDeleteResponse(
        idAnimal   = this.idAnimal,
        animalName = this.animalName,
        deletedAt  = LocalDateTime.now(),
        message    = "Animal deleted successfully"
    )
}

/**
 * Maps an [Animal] domain object to an [AnimalLocationResponse],
 * returning only id, name, species and zipcode.
 */
fun Animal.toLocationResponse() = AnimalLocationResponse(
    animalId   = idAnimal!!,
    animalName = animalName,
    species    = species,
    animalZipcode = animalZipcode
)

