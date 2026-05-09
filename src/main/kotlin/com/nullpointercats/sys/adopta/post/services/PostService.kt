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


    fun updatePost(
        postId: Int,
        updatedPost: Post,
        userId: Int
    ): Post? {

        val postEntityOptional = postRepository.findById(postId.toLong())
        if (postEntityOptional.isEmpty) {
            logger.warn("Attempt to update a non-existing post: $postId")
            return null
        }
        val postEntity = postEntityOptional.get()

        if (postEntity.animal.user.id != userId) {
            logger.warn("Attempt to update post $postId by non-owner user $userId")
            return null
        }

        updatedPost.description?.let { postEntity.description = it }
        updatedPost.status?.let    { postEntity.status = it }

        return try {
            val savedE = postRepository.save(postEntity)
            logger.info("Post $postId updated successfully")
            savedE.toDomain()
        } catch (e: Exception) {
            logger.error("Error updating post $postId: ${e.message}")
            null
        }
    }
    
    /** Returns all posts in the system (public feed). */
    fun getAllPosts(): List<Post> {
        return try {
            postRepository.findAll().map { it.toDomain() }
        } catch (e: Exception) {
            logger.error("Error fetching posts: ${e.message}")
            emptyList()
        }
    }

    /**
     * Returns only the posts whose animal belongs to [userId].
     * Filters in-memory from the full list to avoid adding a custom
     * query to the repository for now.
     */
    fun getPostsByUser(userId: Int): List<Post> {
        return try {
            postRepository.findAll()
                .filter { it.animal.user.id == userId }
                .map    { it.toDomain() }
        } catch (e: Exception) {
            logger.error("Error fetching posts for user $userId: ${e.message}")
            emptyList()
        }
    }   


}