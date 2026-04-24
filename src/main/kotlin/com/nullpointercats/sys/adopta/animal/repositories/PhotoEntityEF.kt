package com.nullpointercats.sys.adopta.animal.repositories

import com.nullpointercats.sys.adopta.animal.domain.Photo
import com.nullpointercats.sys.adopta.animal.entities.AnimalEntity
import com.nullpointercats.sys.adopta.animal.entities.PhotoEntity

/**
 * Converts a [Photo] domain model to an [PhotoEntity].
 */
fun Photo.toEntity(animal: AnimalEntity) : PhotoEntity {
    return PhotoEntity(
        idPhoto = this.idPhoto ?: 0 ,
        url = this.url,
        width = this.width,
        height = this.height,
        animal =  animal
    )
}