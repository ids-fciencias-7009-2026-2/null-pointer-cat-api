package com.nullpointercats.sys.adopta.post.domain

import com.nullpointercats.sys.adopta.animal.domain.Animal
import java.time.LocalDateTime

data class Post(
    val idPost : Int? = null,
    val animal: Animal?,
    val description: String? = null,
    val status: String? = null,
    val cratedAt: LocalDateTime? = LocalDateTime.now()

)