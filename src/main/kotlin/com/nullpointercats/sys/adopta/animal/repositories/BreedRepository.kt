package com.nullpointercats.sys.adopta.animal.repositories

import com.nullpointercats.sys.adopta.animal.entities.BreedEntity
import org.springframework.data.repository.CrudRepository

interface BreedRepository : CrudRepository<BreedEntity, Int> {
}