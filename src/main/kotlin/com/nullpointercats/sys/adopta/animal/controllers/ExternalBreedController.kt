package com.nullpointercats.sys.adopta.animal.controllers

import com.nullpointercats.sys.adopta.animal.dto.external.ExternalBreedDto
import com.nullpointercats.sys.adopta.animal.services.ExternalPetApiClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/external/breeds")
class ExternalBreedController {

    @Autowired
    private lateinit var externalPetApiClient: ExternalPetApiClient

    @GetMapping
    fun getExternalBreeds(@RequestParam species: String): ResponseEntity<List<ExternalBreedDto>> {
        val breeds = externalPetApiClient.fetchBreedsFromExternalApi(species)
        return ResponseEntity.ok(breeds)
    }
}