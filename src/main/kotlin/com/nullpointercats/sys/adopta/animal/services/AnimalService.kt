package com.nullpointercats.sys.adopta.animal.services

import com.nullpointercats.sys.adopta.animal.domain.*
import com.nullpointercats.sys.adopta.animal.dto.request.AnimalRegisterRequest
import com.nullpointercats.sys.adopta.animal.dto.request.AnimalUpdateRequest
import com.nullpointercats.sys.adopta.animal.repositories.*
import com.nullpointercats.sys.adopta.user.domain.toDomain
import com.nullpointercats.sys.adopta.user.repositories.UserRepository
import com.nullpointercats.sys.adopta.user.repositories.toUserEntity
import com.nullpointercats.sys.adopta.user.services.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.text.get
import com.nullpointercats.sys.adopta.favorite.repositories.FavoriteRepository

/**
 * Service layer responsible for executing business logic related to animals.
 * Acts as the bridge between the Controller layer and the Repository layer.
 */
@Service
class AnimalService {
    @Autowired
    lateinit var animalRepository: AnimalRepository

    @Autowired
    lateinit var breedRepository: BreedRepository

    @Autowired
    lateinit var photoRepository: PhotoRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var favoriteRepository: FavoriteRepository


    val logger = LoggerFactory.getLogger(UserService::class.java)

    /**
     * Manage the registration of a new animal.
     */
    @Transactional
    fun addNewAnimal(animal : Animal, breedId : Int?): Animal ? {

        val userEntityOptional = userRepository.findById(animal.publisher.id.toInt())
        if (userEntityOptional  == null) {
            logger.warn("Attempt to register with an animal with invalid user")
            return null
        }
        val userEntity = userEntityOptional.get()
        val breedEntity = breedId?.let { breedRepository.findById(it).orElse(null) }
        val animalEntity = animal.toEntity(userEntity, breedEntity)
        return try {
            val savedEntity = animalRepository.save(animalEntity)
            logger.info("Animal saved with ID: ${savedEntity.idAnimal}")
            savedEntity.toDomain()

        } catch (e: Exception) {
            logger.error("Error saving animal: ${e.message}")
            null
        }

    }
    
    /**
     * * Retrieves a filtered list of animals available for adoption.
     *
     * * All filter parameters are optional.
     * * Allowed values enforced by the DB:
     * *   species → "DOG" | "CAT"
     * *   size    → "small" | "medium" | "large" | "extra_large"
    **/
    
    fun searchAnimals(species: String?,size: String?, zipcode: String?, breedName: String?
    ): List<Animal> {
        
        val normalizedSpecies = species?.uppercase()?.trim()
        val normalizedSize    = size?.lowercase()?.trim()
        val normalizedBreedName = breedName?.lowercase()?.trim() 
        
        logger.info("Searching animals — species=$normalizedSpecies size=$normalizedSize zipcode=$zipcode breedName=$normalizedBreedName")
        
        return animalRepository.findByFilters(normalizedSpecies, normalizedSize, zipcode, normalizedBreedName).map { it.toDomain() }
}

    /**
     * Retrieves a single animal by searching for its id.
     */
    fun getAnimalById(id: Int): Animal {
        return animalRepository.findById(id)
            .map { it.toDomain() }
            .orElseThrow { NoSuchElementException("No se encontró el animal con ID: $id") }
    }

    @Transactional
    fun updateAnimal(
        animalId: Int,
        request: AnimalUpdateRequest,
        userId: Int
    ): Animal? {

        val animalEntityOptional = animalRepository.findById(animalId)
        if (animalEntityOptional.isEmpty) {
            logger.warn("Attempt to update non-existing animal: $animalId")
            return null
        }
        val animalEntity = animalEntityOptional.get()


        if (animalEntity.user.id?.toLong() != userId.toLong()) {
            logger.warn("User $userId tried to update animal $animalId owned by another user")
            return null
        }

        request.animalName?.let   { animalEntity.animalName    = it }
        request.species?.let      { animalEntity.species       = it }
        request.description?.let  { animalEntity.description   = it }
        request.size?.let         { animalEntity.size          = it }
        request.animalZipcode?.let{ animalEntity.animalZipcode = it }
        request.dateOfBirth?.let  { animalEntity.dateOfBirth   = it }

        // Breed is optional
        request.breedId?.let { id ->
            val breedEntity = breedRepository.findById(id).orElse(null)
            if (breedEntity == null) {
                logger.warn("Breed $id not found, skipping breed update")
            } else {
                animalEntity.breed = breedEntity
            }
        }

        return try {
            val saved = animalRepository.save(animalEntity)
            logger.info("Animal $animalId updated successfully")
            saved.toDomain()
        } catch (e: Exception) {
            logger.error("Error updating animal $animalId: ${e.message}")
            null
        }
    }

    fun getAnimals(): List<Animal> {
        return try {
            animalRepository.findAll().map { it.toDomain() }
        } catch (e: Exception) {
            logger.error("Error fetching animals: ${e.message}")
            emptyList()
        }
    }

    fun getAnimalsFromUser(id : Int): List<Animal> {
        return try {
            animalRepository.findAnimalsByUserId(id).map { it.toDomain() }
        } catch (e: Exception) {
            logger.error("Error fetching animals: ${e.message}")
            emptyList()
        }
    }

    /**
     * Deletes an animal post owned by the requesting user.
     *
     * Returns the deleted animal  or null when the animal does not exist or the requester is
     * not its publisher.
     */
    @Transactional
    fun deleteAnimal(animalId: Int, userId: Int): Animal? {

        val animalEntityOptional = animalRepository.findById(animalId)
        if (animalEntityOptional.isEmpty) {
            logger.warn("Attempt to delete non-existing animal: $animalId")
            return null
        }
        val animalEntity = animalEntityOptional.get()

        if (animalEntity.user.id?.toLong() != userId.toLong()) {
            logger.warn("User $userId tried to delete animal $animalId owned by another user")
            return null
        }

        val snapshot = animalEntity.toDomain()

        return try {
            val removedFavorites = favoriteRepository.deleteByAnimalId(animalId)
            logger.info("Removed $removedFavorites favorite(s) referencing animal $animalId")
            animalRepository.delete(animalEntity)
            logger.info("Animal $animalId deleted successfully")
            snapshot
        } catch (e: Exception) {
            logger.error("Error deleting animal $animalId: ${e.message}")
            null
        }
    }

}

