package com.nullpointercats.sys.adopta.favorite.repositories

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import com.nullpointercats.sys.adopta.favorite.entities.FavoriteEntity
import org.springframework.data.repository.CrudRepository

interface FavoriteRepository : CrudRepository<FavoriteEntity, Int> {
    
    fun existsByUserIdAndAnimalIdAnimal(userId: Int, animalId: Int): Boolean
    @Query("SELECT f FROM FavoriteEntity f WHERE f.user.id = :userId")
    fun findByUserId(@Param("userId") userId: Int): List<FavoriteEntity>
}
