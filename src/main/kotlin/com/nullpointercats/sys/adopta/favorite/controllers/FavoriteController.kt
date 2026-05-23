package com.nullpointercats.sys.adopta.favorite.controllers

import com.nullpointercats.sys.adopta.animal.domain.toResponse
import com.nullpointercats.sys.adopta.animal.dto.response.AnimalResponse
import com.nullpointercats.sys.adopta.favorite.dto.response.FavoriteResponse
import com.nullpointercats.sys.adopta.favorite.services.FavoriteService
import com.nullpointercats.sys.adopta.user.domain.User
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/favorites")
class FavoriteController {

    val logger = LoggerFactory.getLogger(FavoriteController::class.java)

    @Autowired lateinit var favoriteService: FavoriteService

    @PostMapping("/{animalId}")
    fun addFavorite(
        @PathVariable animalId: Int,
        @RequestAttribute("authenticatedUser") userFound: User
    ): ResponseEntity<FavoriteResponse> {

        logger.info("[favorites] [ATTEMPT] User ${userFound.email} on animal $animalId")

        val favorite = favoriteService.addFavorite(userFound.id.toInt(), animalId)

        if (favorite == null) {
            logger.warn("[favorites] [FAILED] Could not save favorite")
            return ResponseEntity.status(409).build()
        }

        logger.info("[favorites] [SUCCESS] Favorite saved")
        return ResponseEntity.ok(
            FavoriteResponse(
                userId = favorite.userId,
                animalId = favorite.animalId,
                savedAt = favorite.savedAt
            )
        )
    }

    @GetMapping("/me")
    fun getMyFavorites(
        @RequestAttribute("authenticatedUser") userFound: User
    ): ResponseEntity<List<AnimalResponse>> {
        logger.info("[GET /favorites/me] User ${userFound.email} fetching their favorites")
        val animals = favoriteService.getFavoritesByUser(userFound.id.toInt())
        return ResponseEntity.ok(animals.map { it.toResponse() })
    }
}