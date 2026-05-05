package com.nullpointercats.sys.adopta.animal.repositories

import com.nullpointercats.sys.adopta.animal.entities.AnimalEntity
import org.springframework.data.jpa.repository.Query 
import org.springframework.data.repository.query.Param
import org.springframework.data.repository.CrudRepository

/**
 * Data Repository for managing [AnimalEntity] persistence.
 */
interface AnimalRepository : CrudRepository<AnimalEntity, Int> {
    @Query("""
        SELECT DISTINCT a
        FROM AnimalEntity a
        LEFT JOIN FETCH a.breed b
        LEFT JOIN FETCH a.photos p
        WHERE (:species IS NULL OR a.species       = :species)
          AND (:size    IS NULL OR a.size          = :size)
          AND (:zipcode IS NULL OR a.animalZipcode = :zipcode)
          AND (:breedName IS NULL OR LOWER(b.breedName) LIKE LOWER(CONCAT('%', :breedName, '%')))
          ORDER BY a.publishedAt DESC
    """)
    fun findByFilters(
        @Param("species") species: String?,
        @Param("size")    size: String?,
        @Param("zipcode") zipcode: String?,
        @Param("breedName") breedName: String?
    ): List<AnimalEntity>
}