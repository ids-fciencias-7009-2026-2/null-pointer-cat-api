package com.nullpointercats.sys.adopta.animal.repositories

import com.nullpointercats.sys.adopta.animal.entities.PhotoEntity
import org.springframework.data.repository.CrudRepository

/**
 * Data Repository for managing [PhotoEntity] persistence.
 */
interface PhotoRepository : CrudRepository<PhotoEntity, Int> {
}