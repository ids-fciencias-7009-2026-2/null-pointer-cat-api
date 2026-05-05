package com.nullpointercats.sys.adopta.animal.services

import com.nullpointercats.sys.adopta.animal.domain.*
import com.nullpointercats.sys.adopta.animal.dto.request.AnimalRegisterRequest
import com.nullpointercats.sys.adopta.animal.repositories.*
import com.nullpointercats.sys.adopta.user.domain.toDomain
import com.nullpointercats.sys.adopta.user.repositories.UserRepository
import com.nullpointercats.sys.adopta.user.repositories.toUserEntity
import com.nullpointercats.sys.adopta.user.services.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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

}