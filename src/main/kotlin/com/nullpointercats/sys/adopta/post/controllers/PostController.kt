package com.nullpointercats.sys.adopta.post.controllers

import com.nullpointercats.sys.adopta.post.domain.toDomain
import com.nullpointercats.sys.adopta.post.domain.toResponse
import com.nullpointercats.sys.adopta.post.dto.request.PostRegisterRequest
import com.nullpointercats.sys.adopta.post.dto.response.PostRegisterResponse
import com.nullpointercats.sys.adopta.post.services.PostService
import com.nullpointercats.sys.adopta.user.domain.User
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/post")
class PostController {
    val logger: Logger = LoggerFactory.getLogger(PostController::class.java)

    @Autowired
    lateinit var postService: PostService

    @PostMapping("/register")
    fun registerPost(
        @RequestBody request: PostRegisterRequest,
        @RequestAttribute("authenticatedUser") userFound: User)
    : ResponseEntity<PostRegisterResponse>{

        logger.info("[post/register] [ATTEMPT] Attempting new post from ${userFound.email}")

        val postDomain = request.toDomain()
        val post = postService.addNewPost(postDomain, request.idAnimal, userFound.id.toInt())

        if(post == null) {
            logger.warn("[post/register] [FAILED] Failed to create new post from ${userFound.email}")
            return ResponseEntity.status(409).build()
        }

        logger.info("[post/register] [SUCCESS] New post $post")
        return ResponseEntity.ok( post.toResponse() )
    }



}