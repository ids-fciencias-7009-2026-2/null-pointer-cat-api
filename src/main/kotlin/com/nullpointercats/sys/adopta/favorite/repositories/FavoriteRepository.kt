package com.nullpointercats.sys.adopta.favorite.repositories

import com.nullpointercats.sys.adopta.favorite.entities.FavoriteEntity
import org.springframework.data.repository.CrudRepository

interface FavoriteRepository : CrudRepository<FavoriteEntity, Int> {
    
    fun existsByUserIdAndAnimalIdAnimal(userId: Int, animalId: Int): Boolean
}
