package com.nullpointercats.sys.adopta.post.domain

import com.nullpointercats.sys.adopta.animal.domain.Animal
import com.nullpointercats.sys.adopta.animal.domain.toDomain
import com.nullpointercats.sys.adopta.post.dto.request.PostRegisterRequest
import com.nullpointercats.sys.adopta.post.dto.response.PostRegisterResponse
import com.nullpointercats.sys.adopta.post.dto.request.PostUpdateRequest
import com.nullpointercats.sys.adopta.post.dto.response.PostUpdateResponse
import com.nullpointercats.sys.adopta.post.dto.response.PostFeedResponse
import com.nullpointercats.sys.adopta.post.entities.PostEntity
import com.nullpointercats.sys.adopta.user.domain.User
import java.time.LocalDateTime

fun Post.toResponse() : PostRegisterResponse{
    return PostRegisterResponse(
        idPost = this.idPost?:0,
        idAnimal = this.animal?.idAnimal?:0,
        description = this.description,
        status = this.status,
        createdAt = LocalDateTime.now(),
    )
}

fun PostRegisterRequest.toDomain(
    animal: Animal?=null,
) : Post{
    return Post(
        idPost = null,
        description = this.description,
        status = this.status,
        cratedAt = LocalDateTime.now(),
        animal = animal
    )
}

fun PostEntity.toDomain(): Post {
    return Post(
        idPost = this.idPost,
        animal = this.animal.toDomain(),
        description = this.description,
        status = this.status,
        cratedAt = this.createdAt
    )
}

fun PostUpdateRequest.toDomain(): Post {
    return Post(
        description = this.description,
        status = this.status,
        animal = null
    )
}

fun Post.toUpdateResponse(): PostUpdateResponse {
    return PostUpdateResponse(
        idPost = this.idPost!!,
        description = this.description,
        status = this.status,
        createdAt = this.cratedAt!!,
        idAnimal = this.animal!!.idAnimal!!
    )
}

fun Post.toFeedResponse(): PostFeedResponse {
    val animal: Animal = this.animal!!
    val publisher: User = animal.publisher
    return PostFeedResponse(
        idPost        = this.idPost!!,
        description   = this.description,
        status        = this.status!!,
        createdAt     = this.cratedAt!!,

        publisherUsername  = publisher.username,
        publisherFirstname = publisher.firstname,
        publisherLastname  = publisher.lastname,
        publisherId       = this.animal!!.publisher.id.toInt()!!,

        idAnimal      = this.animal!!.idAnimal!!,
        animalName    = this.animal.animalName,
        species       = this.animal.species,
        size          = this.animal.size,
        dateOfBirth   = this.animal.dateOfBirth,
        animalZipcode = this.animal.animalZipcode,
        breedName     = this.animal.breed?.breedName,
        animalDescription = this.animal.description,
        photos        = this.animal.photos.map { it.url }
    )
}