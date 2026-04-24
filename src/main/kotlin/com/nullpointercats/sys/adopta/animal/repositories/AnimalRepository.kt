package com.nullpointercats.sys.adopta.animal.repositories

import com.nullpointercats.sys.adopta.animal.entities.AnimalEntity
import org.springframework.data.repository.CrudRepository

interface AnimalRepository : CrudRepository<AnimalEntity, Int> {
}