package com.nullpointercats.sys.adopta.post.domain

import com.nullpointercats.sys.adopta.animal.domain.Animal
import com.nullpointercats.sys.adopta.animal.domain.toDomain
import com.nullpointercats.sys.adopta.post.dto.request.PostRegisterRequest
import com.nullpointercats.sys.adopta.post.dto.response.PostRegisterResponse
import com.nullpointercats.sys.adopta.post.dto.request.PostUpdateRequest
import com.nullpointercats.sys.adopta.post.dto.response.PostUpdateResponse
import com.nullpointercats.sys.adopta.post.entities.PostEntity
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

// Convierte PostUpdateRequest → Post (dominio parcial, sin animal)
fun PostUpdateRequest.toDomain(): Post {
    return Post(
        description = this.description,
        status = this.status,
        animal = null
    )
}

// Convierte Post → PostUpdateResponse
fun Post.toUpdateResponse(): PostUpdateResponse {
    return PostUpdateResponse(
        idPost = this.idPost!!,
        description = this.description,
        status = this.status,
        createdAt = this.cratedAt!!,
        idAnimal = this.animal!!.idAnimal!!
    )
}