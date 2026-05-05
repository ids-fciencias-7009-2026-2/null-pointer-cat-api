package com.nullpointercats.sys.adopta.animal.controllers

import com.nullpointercats.sys.adopta.animal.domain.Animal
import com.nullpointercats.sys.adopta.animal.domain.toDomain
import com.nullpointercats.sys.adopta.animal.domain.toResponse
import com.nullpointercats.sys.adopta.animal.dto.request.AnimalRegisterRequest
import com.nullpointercats.sys.adopta.animal.dto.response.AnimalRegisterResponse
import com.nullpointercats.sys.adopta.animal.services.AnimalService
import com.nullpointercats.sys.adopta.user.domain.User
import com.nullpointercats.sys.adopta.user.services.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.jvm.java

/**
 * REST Controller for managing animal-related operations.
 * */

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
     *
     * URL:    http://localhost:8080/animals/register
     * Method: POST
     */
    @PostMapping("/register")
    fun addAnimal(
        @RequestBody request: AnimalRegisterRequest,
        @RequestAttribute("authenticatedUser") userFound: User
    ): ResponseEntity<AnimalRegisterResponse> {

        logger.info("[animals/register] [ATTEMPT] Attempting new animal registration with from ${userFound.email}")

        val animalDomain = request.toDomain(userFound, null)
        val animalSaved = animalService.addNewAnimal(animalDomain, request.breedId)

        if (animalSaved == null) {
            logger.warn("[animals/register] [FAILED] Error adding new animal registration.")
            return ResponseEntity.status(409).build()
        }

        logger.info("[animals/register] [SUCCESS] Animal registered successfully ")
        return ResponseEntity.ok(animalSaved.toResponse())
    }
    
    /**
    * Endpoint to search animals available for adoption using optional filters.
    *
    * URL:    GET http://localhost:8080/animals
    * Params (all optional):
    *   species  → DOG | CAT
    *   size     → small | medium | large | extra_large
    *   zipcode  → e.g. 06600
    *   breedId  → numeric breed ID
    *
    * Returns an empty list when no animals match — never 404.
    */
    
    @GetMapping
    fun searchAnimals(
        @RequestParam(required = false) species: String?,
        @RequestParam(required = false) size: String?,
        @RequestParam(required = false) zipcode: String?,
        @RequestParam(required = false) breedId: Int?,
        @RequestAttribute("authenticatedUser") userFound: User
    ): ResponseEntity<List<AnimalSearchResponse>> {
        
        logger.info(
            "[GET /animals] [ATTEMPT] From ${userFound.email} " +
            "— species=$species size=$size zipcode=$zipcode breedId=$breedId"
            )
            
        val results = animalService.searchAnimals(species, size, zipcode, breedId).map { it.toSearchResponse() }
        
        logger.info("[GET /animals] [SUCCESS] ${results.size} animals found")
        return ResponseEntity.ok(results)
    }
    
}