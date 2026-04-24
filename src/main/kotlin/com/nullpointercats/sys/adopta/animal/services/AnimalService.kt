package com.nullpointercats.sys.adopta.animal.services

import com.nullpointercats.sys.adopta.animal.domain.*
import com.nullpointercats.sys.adopta.animal.dto.request.AnimalRegisterRequest
import com.nullpointercats.sys.adopta.animal.repositories.*
import com.nullpointercats.sys.adopta.user.domain.toDomain
import com.nullpointercats.sys.adopta.user.repositories.UserRepository
import com.nullpointercats.sys.adopta.user.services.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

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
    fun addNewAnimal(request : AnimalRegisterRequest, publisherId : Int): Animal ? {

        val userEntityOptional = userRepository.findById(publisherId)
        if (userEntityOptional.isEmpty) {
            logger.warn("Attempt to register a animal with invalid user ID: $publisherId")
            return null
        }

        val userEntity = userEntityOptional.get()
        val userDomain = userEntity.toDomain()

        val breedEntity = if (request.breedId != null) { breedRepository.findById(request.breedId).orElse(null)
        } else { null }
        val breedDomain = breedEntity?.toDomain()

        val animalDomain = request.toDomain(userDomain, breedDomain)
        val animalEntity = animalDomain.toEntity(userEntity, breedEntity)

        val savedEntity = animalRepository.save(animalEntity)
        logger.info("Animal saved with ID: ${savedEntity.idAnimal}")

        return savedEntity.toDomain()
    }

}