package com.nullpointercats.sys.adopta.geocoding.controllers

import com.nullpointercats.sys.adopta.geocoding.dto.GeocodingResponse
import com.nullpointercats.sys.adopta.geocoding.services.GeocodingService
import com.nullpointercats.sys.adopta.user.domain.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * REST Controller for resolving postal codes into geographic coordinates.
 * Coordinates are used by the frontend to render approximate animal
 * locations on the map without exposing exact addresses.
 */

@RestController
@RequestMapping("/geocoding")
class GeocodingController {

    val logger: Logger = LoggerFactory.getLogger(GeocodingController::class.java)

    @Autowired
    lateinit var geocodingService: GeocodingService

    @GetMapping("/{zipcode}")
    fun getCoordinates(
        @PathVariable zipcode: String,
        @RequestAttribute("authenticatedUser") userFound: User
    ): ResponseEntity<GeocodingResponse> {

        logger.info("[GET /geocoding/$zipcode] [ATTEMPT] User ${userFound.email} requesting coordinates")

        val coordinates = geocodingService.getCoordinatesByZipcode(zipcode)

        if (coordinates == null) {
            logger.warn("[GET /geocoding/$zipcode] [FAILED] No coordinates found")
            return ResponseEntity.status(404).build()
        }

        logger.info("[GET /geocoding/$zipcode] [SUCCESS] lat=${coordinates.lat} lng=${coordinates.lng}")
        return ResponseEntity.ok(coordinates)
    }
}