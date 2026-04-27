package com.nullpointercats.sys.adopta.post.services

import com.nullpointercats.sys.adopta.animal.repositories.AnimalRepository
import com.nullpointercats.sys.adopta.post.domain.Post
import com.nullpointercats.sys.adopta.post.domain.toDomain
import com.nullpointercats.sys.adopta.post.repositories.PostRepository
import com.nullpointercats.sys.adopta.post.repositories.toEntity
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PostService {

    val logger = LoggerFactory.getLogger(PostService::class.java)

    @Autowired
    lateinit var postRepository: PostRepository

    @Autowired
    lateinit var animalRepository: AnimalRepository

    fun addNewPost(
        post :  Post,
        animalId : Int,
        userId : Int): Post? {

        val animalEntityOptional = animalRepository.findById(animalId)
        if (animalEntityOptional.isEmpty) {
            logger.warn("Attempt to register with an animal null")
            return null
        }
        val animalEntity = animalEntityOptional.get()

        if(animalEntity.user.id != userId) {
            logger.warn("Attempt to register with an animal from other user")
            return null
        }

        val postEntity = post.toEntity(animalEntity)

        return try {
            val savedE = postRepository.save(postEntity)
            logger.info("Post saved with ID: ${savedE.idPost}")
            return savedE.toDomain()

        } catch (e: Exception) {
            logger.error("Error saving post: ${e.message}")
            null
        }
    }




}