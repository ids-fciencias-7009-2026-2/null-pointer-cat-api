package com.nullpointercats.sys.adopta.post.dto.request


data class PostRegisterRequest(
    val idAnimal: Int,
    val description: String? = null,
    val status: String? = null,
)