package com.nullpointercats.sys.adopta.post.repositories

import com.nullpointercats.sys.adopta.animal.entities.AnimalEntity
import com.nullpointercats.sys.adopta.post.domain.Post
import com.nullpointercats.sys.adopta.post.entities.PostEntity

fun Post.toEntity(animalEntity : AnimalEntity): PostEntity {
    return PostEntity(
        idPost = this.idPost ?:0,
        animal = animalEntity,
        description = this.description,
        status = this.status,
    )
}