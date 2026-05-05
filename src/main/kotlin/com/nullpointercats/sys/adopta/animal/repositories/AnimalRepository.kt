package com.nullpointercats.sys.adopta.animal.repositories

import com.nullpointercats.sys.adopta.animal.entities.AnimalEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface AnimalRepository : CrudRepository<AnimalEntity, Int> {

    @Query(value = """
        SELECT DISTINCT a.* 
        FROM animal a
        LEFT JOIN breed b ON b.id_breed = a.id_breed
        WHERE (:species   IS NULL OR a.species        = :species)
          AND (:size      IS NULL OR a.size           = :size)
          AND (:zipcode   IS NULL OR a.animal_zipcode = :zipcode)
          AND (:breedName IS NULL OR (b.id_breed IS NOT NULL AND lower(b.breed_name) LIKE lower(('%' || :breedName || '%'))))
        ORDER BY a.published_at DESC
    """, nativeQuery = true)
    fun findByFilters(
        @Param("species")   species: String?,
        @Param("size")      size: String?,
        @Param("zipcode")   zipcode: String?,
        @Param("breedName") breedName: String?
    ): List<AnimalEntity>
}