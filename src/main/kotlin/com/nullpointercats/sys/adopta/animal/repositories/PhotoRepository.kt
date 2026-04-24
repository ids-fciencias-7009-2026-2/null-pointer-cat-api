package com.nullpointercats.sys.adopta.animal.repositories

import com.nullpointercats.sys.adopta.animal.entities.PhotoEntity
import org.springframework.data.repository.CrudRepository

interface PhotoRepository : CrudRepository<PhotoEntity, Int> {
}