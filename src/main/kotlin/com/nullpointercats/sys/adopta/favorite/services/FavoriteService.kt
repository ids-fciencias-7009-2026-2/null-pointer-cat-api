package com.nullpointercats.sys.adopta.favorite.services

import com.nullpointercats.sys.adopta.animal.domain.Animal
import com.nullpointercats.sys.adopta.animal.domain.toDomain
import com.nullpointercats.sys.adopta.animal.repositories.AnimalRepository
import com.nullpointercats.sys.adopta.favorite.domain.Favorite
import com.nullpointercats.sys.adopta.favorite.domain.toDomain
import com.nullpointercats.sys.adopta.favorite.repositories.FavoriteRepository
import com.nullpointercats.sys.adopta.favorite.repositories.toFavoriteEntity
import com.nullpointercats.sys.adopta.user.repositories.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class FavoriteService {

    @Autowired lateinit var favoriteRepository: FavoriteRepository
    @Autowired lateinit var animalRepository: AnimalRepository
    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var mailSender: JavaMailSender

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

            sendEmail(
                toEmail = animalEntity.user.email,
                ownerName = animalEntity.user.firstname,
                interestedName = "${userEntity.firstname} ${userEntity.lastname}",
                interestedEmail = userEntity.email,
                animalName = animalEntity.animalName,
                animalSpecies = animalEntity.species,
                animalDescription = animalEntity.description ?: "Sin descripción",
                animalZipcode = animalEntity.animalZipcode
            )

            saved.toDomain()
        } catch (e: Exception) {
            logger.error("Error saving favorite: ${e.message}")
            null
        }
    }

    fun sendEmail(
        toEmail: String,
        ownerName: String,
        interestedName: String,
        interestedEmail: String,
        animalName: String,
        animalSpecies: String,
        animalDescription: String,
        animalZipcode: String
    ) {
        try {
            val message = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, true, "UTF-8")

            helper.setTo(toEmail)
            helper.setSubject("🐾 ¡Alguien está interesado en adoptar a $animalName!")
            helper.setFrom("AdoptaPet <rebeca07e.r@gmail.com>")

            val html = """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px;">
                    <h2 style="color: #1d423f;">🐾 ¡Buenas noticias, $ownerName!</h2>
                    <p>Un usuario está interesado en adoptar a tu mascota <strong>$animalName</strong>.</p>
                    
                    <div style="background: #f2f1eb; padding: 16px; border-radius: 8px; margin: 20px 0;">
                        <h3 style="color: #1d423f;">Ficha del animal</h3>
                        <p><strong>Nombre:</strong> $animalName</p>
                        <p><strong>Especie:</strong> $animalSpecies</p>
                        <p><strong>Descripción:</strong> $animalDescription</p>
                        <p><strong>Código postal:</strong> $animalZipcode</p>
                    </div>
                    
                    <div style="background: #88b2a6; padding: 16px; border-radius: 8px; margin: 20px 0;">
                        <h3 style="color: white;">Datos del interesado</h3>
                        <p style="color: white;"><strong>Nombre:</strong> $interestedName</p>
                        <p style="color: white;"><strong>Correo:</strong> $interestedEmail</p>
                    </div>
                    
                    <p>Contáctale para coordinar la adopción. 🐶</p>
                    <p style="color: #666; font-size: 12px;">— AdoptaPet</p>
                </div>
            """.trimIndent()

            helper.setText(html, true)
            mailSender.send(message)
            logger.info("Email sent successfully to $toEmail")
        } catch (e: Exception) {
            logger.error("Error sending email: ${e.message}")
        }
    }

    fun getFavoritesByUser(userId: Int): List<Animal> {
        return try {
            favoriteRepository.findByUserId(userId).map { it.animal.toDomain() }
        } catch (e: Exception) {
            logger.error("Error fetching favorites for user $userId: ${e.message}")
            emptyList()
        }
    }

}
