package com.nullpointercats.sys.adopta.post.repositories

import com.nullpointercats.sys.adopta.post.entities.PostEntity
import org.springframework.data.repository.CrudRepository

interface PostRepository  : CrudRepository<PostEntity, Long> {

}