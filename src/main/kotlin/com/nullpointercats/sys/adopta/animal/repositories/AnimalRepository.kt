package com.nullpointercats.sys.adopta.animal.repositories

import com.nullpointercats.sys.adopta.animal.entities.AnimalEntity
import org.springframework.data.repository.CrudRepository

/**
 * Data Repository for managing [AnimalEntity] persistence.
 */
interface AnimalRepository : CrudRepository<AnimalEntity, Int> {
}