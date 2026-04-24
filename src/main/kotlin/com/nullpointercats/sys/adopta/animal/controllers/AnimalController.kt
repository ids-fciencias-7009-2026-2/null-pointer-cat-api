package com.nullpointercats.sys.adopta.animal.controllers

import com.nullpointercats.sys.adopta.animal.domain.Animal
import com.nullpointercats.sys.adopta.animal.dto.request.AnimalRegisterRequest
import com.nullpointercats.sys.adopta.animal.services.AnimalService
import com.nullpointercats.sys.adopta.user.services.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
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
        @RequestBody registerAnimalRequest: AnimalRegisterRequest,
        @RequestHeader("Authorization", required = false) token: String?
    ): ResponseEntity<Animal> {

        val cleanToken = token?.removePrefix("Bearer ")?.trim().orEmpty()
        logger.info("[animals/register] [ATTEMPT]  From token [${cleanToken.take(10)}]")

        if(cleanToken.isEmpty()){
            logger.warn("[animals/register] [ATTEMPT]  No token given.")
            return ResponseEntity.status(401).build()
        }

        val userFound = userService.findByToken(cleanToken.orEmpty())

        if (userFound == null) {
            logger.warn("[animals/register] [ATTEMPT]  No user active found. Token may be invalid of expired.")
            return ResponseEntity.status(401).build()
        }

        logger.info("[animals/register] [ATTEMPT] Attempting new animal registration with from ${userFound.email}")

        val animalSaved = animalService.addNewAnimal(registerAnimalRequest, userFound.id.toInt())

        if (animalSaved == null) {
            logger.warn("[animals/register] [FAILED] Error adding new animal registration.")
            return ResponseEntity.status(409).build()
        }

        logger.info("[animals/register] [SUCCESS] Animal registered successfully ")
        return ResponseEntity.ok(animalSaved)
    }
}