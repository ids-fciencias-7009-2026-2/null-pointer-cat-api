package com.nullpointercats.sys.adopta.favorite.services

import com.nullpointercats.sys.adopta.animal.repositories.AnimalRepository
import com.nullpointercats.sys.adopta.favorite.domain.Favorite
import com.nullpointercats.sys.adopta.favorite.domain.toDomain
import com.nullpointercats.sys.adopta.favorite.repositories.FavoriteRepository
import com.nullpointercats.sys.adopta.favorite.repositories.toFavoriteEntity
import com.nullpointercats.sys.adopta.user.repositories.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service
class FavoriteService {

    @Autowired lateinit var favoriteRepository: FavoriteRepository
    @Autowired lateinit var animalRepository: AnimalRepository
    @Autowired lateinit var userRepository: UserRepository

    val logger = LoggerFactory.getLogger(FavoriteService::class.java)

    fun addFavorite(userId: Int, animalId: Int): Favorite? {

        if (favoriteRepository.existsByUserIdAndAnimalIdAnimal(userId, animalId)) {
            logger.warn("User $userId already marked interest in animal $animalId")
            return null
        }

        val userEntity = userRepository.findById(userId).orElse(null) ?: return null
        val animalEntity = animalRepository.findById(animalId).orElse(null) ?: return null

        val favoriteEntity = toFavoriteEntity(userEntity, animalEntity)

        return try {
            val saved = favoriteRepository.save(favoriteEntity)
            logger.info("Favorite saved for user $userId on animal $animalId")

            // Mandar correo al dueño del animal
            val ownerEmail = animalEntity.user.email
            val ownerName = animalEntity.user.firstname
            val interestedEmail = userEntity.email
            val interestedName = userEntity.firstname
            val animalName = animalEntity.animalName

            sendEmail(ownerEmail, ownerName, interestedName, interestedEmail, animalName)

            saved.toDomain()
        } catch (e: Exception) {
            logger.error("Error saving favorite: ${e.message}")
            null
        }
    }

    fun sendEmail(
        toEmail: String,
        toName: String,
        interestedName: String,
        interestedEmail: String,
        animalName: String
    ) {
        try {
            val apiKey = System.getProperty("RESEND_API_KEY")
            val body = """
                {
                    "from": "AdoptaPet <onboarding@resend.dev>",
                    "to": ["$toEmail"],
                    "subject": "¡Alguien está interesado en adoptar a $animalName!",
                    "html": "<p>Hola $toName,</p><p><strong>$interestedName</strong> ($interestedEmail) está interesado en adoptar a <strong>$animalName</strong>.</p><p>Contáctale para coordinar la adopción.</p><p>— AdoptaPet</p>"
                }
            """.trimIndent()

            val request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.resend.com/emails"))
                .header("Authorization", "Bearer $apiKey")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build()

            val response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString())

            logger.info("Email sent to $toEmail — status: ${response.statusCode()}")
        } catch (e: Exception) {
            logger.error("Error sending email: ${e.message}")
        }
    }
}
