package com.nullpointercats.sys.adopta.favorite.repositories

import com.nullpointercats.sys.adopta.favorite.entities.FavoriteEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface FavoriteRepository : CrudRepository<FavoriteEntity, Int> {
    
    fun existsByUserIdAndAnimalIdAnimal(userId: Int, animalId: Int): Boolean

    @Modifying
    @Query("DELETE FROM FavoriteEntity f WHERE f.animal.idAnimal = :animalId")
    fun deleteByAnimalId(@Param("animalId") animalId: Int): Int
}
