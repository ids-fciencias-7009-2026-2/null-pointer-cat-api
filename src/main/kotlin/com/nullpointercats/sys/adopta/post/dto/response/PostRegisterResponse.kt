package com.nullpointercats.sys.adopta.post.dto.response

import java.time.LocalDateTime

data class PostRegisterResponse(
    val idPost : Int,
    val description: String? = null,
    val status: String? = null,
    val createdAt : LocalDateTime,

    val idAnimal: Int
)