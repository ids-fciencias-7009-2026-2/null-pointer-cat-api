package com.nullpointercats.sys.adopta.animal.controllers

import com.nullpointercats.sys.adopta.animal.domain.Animal
import com.nullpointercats.sys.adopta.animal.domain.toDomain
import com.nullpointercats.sys.adopta.animal.domain.toResponse
import com.nullpointercats.sys.adopta.animal.domain.toSearchResponse
import com.nullpointercats.sys.adopta.animal.dto.request.AnimalRegisterRequest
import com.nullpointercats.sys.adopta.animal.dto.response.AnimalRegisterResponse
import com.nullpointercats.sys.adopta.animal.dto.response.AnimalSearchResponse
import com.nullpointercats.sys.adopta.animal.services.AnimalService
import com.nullpointercats.sys.adopta.user.domain.User
import com.nullpointercats.sys.adopta.user.services.UserService
import com.nullpointercats.sys.adopta.animal.domain.toUpdateResponse
import com.nullpointercats.sys.adopta.animal.dto.request.AnimalUpdateRequest
import com.nullpointercats.sys.adopta.animal.dto.response.AnimalUpdateResponse
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import kotlin.jvm.java

/**
 * REST Controller for managing animal-related operations.
 */
@RestController
@RequestMapping("/animals")
class AnimalController {
    val logger: Logger = LoggerFactory.getLogger(AnimalController::class.java)

    @Autowired
    lateinit var animalService: AnimalService

    @Autowired
    lateinit var userService: UserService

    /**
     * Endpoint to register a new animal in the system.
     * URL: POST http://localhost:8080/animals/register
     */
    @PostMapping("/register")
    fun addAnimal(
        @RequestBody request: AnimalRegisterRequest,
        @RequestAttribute("authenticatedUser") userFound: User
    ): ResponseEntity<AnimalRegisterResponse> {

        logger.info("[animals/register] [ATTEMPT] Attempting new animal registration from ${userFound.email}")

        val animalDomain = request.toDomain(userFound, null)

        if (animalDomain.photos.isEmpty()) {
            logger.warn("[animals/register] No photos to add. We need at least one photo.")
            return ResponseEntity.status(409).build()
        }

        val animalSaved = animalService.addNewAnimal(animalDomain, request.breedId)

        if (animalSaved == null) {
            logger.warn("[animals/register] [FAILED] Error adding new animal registration.")
            return ResponseEntity.status(409).build()
        }

        logger.info("[animals/register] [SUCCESS] Animal registered successfully")
        return ResponseEntity.ok(animalSaved.toResponse())
    }

    /**
     * Endpoint to search animals available for adoption using optional filters.
     * URL: GET http://localhost:8080/animals
     */
    @GetMapping
    fun searchAnimals(
        @RequestParam(required = false) species: String?,
        @RequestParam(required = false) size: String?,
        @RequestParam(required = false) zipcode: String?,
        @RequestParam(required = false) breedName: String?,
        @RequestAttribute("authenticatedUser") userFound: User
    ): ResponseEntity<List<AnimalSearchResponse>> {

        logger.info(
            "[GET /animals] [ATTEMPT] From ${userFound.email} " +
            "— species=$species size=$size zipcode=$zipcode breedName=$breedName"
        )

        val results = animalService.searchAnimals(species, size, zipcode, breedName).map { it.toSearchResponse() }

        logger.info("[GET /animals] [SUCCESS] ${results.size} animals found")
        return ResponseEntity.ok(results)
    }

    /**
     * Endpoint to get a specific animal by its id.
     * URL: GET http://localhost:8080/animals/{id}
     */
    @GetMapping("/{id}")
    fun getAnimalInfo(
        @PathVariable id: Int,
        @RequestAttribute("authenticatedUser") userFound: User
    ): ResponseEntity<AnimalSearchResponse> {

        logger.info("[GET /animals/$id] [ATTEMPT] User ${userFound.email} is viewing pet $id")

        val animal = animalService.getAnimalById(id)
        
        logger.info("[GET /animals/$id] [SUCCESS] Animal ${animal.animalName} found")
        return ResponseEntity.ok(animal.toSearchResponse())
    }

    @PutMapping("/{id}")
    fun updateAnimal(
        @PathVariable id: Int,
        @RequestBody request: AnimalUpdateRequest,
        @RequestAttribute("authenticatedUser") userFound: User
    ): ResponseEntity<AnimalUpdateResponse> {

        logger.info("[animals/update] [ATTEMPT] User ${userFound.email} updating animal $id")

        val updated = animalService.updateAnimal(id, request, userFound.id.toInt())

        if (updated == null) {
            logger.warn("[animals/update] [FAILED] Could not update animal $id for ${userFound.email}")
            return ResponseEntity.status(409).build()
        }

        logger.info("[animals/update] [SUCCESS] Animal $id updated")
        return ResponseEntity.ok(updated.toUpdateResponse())
    }
}