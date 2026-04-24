package com.nullpointercats.sys.adopta.animal.domain

import com.nullpointercats.sys.adopta.animal.entities.PhotoEntity

fun PhotoEntity.toDomain() : Photo {
    return Photo(
        idPhoto = this.idPhoto,
        url = this.url,
        width = this.width,
        height = this.height,
    )
}