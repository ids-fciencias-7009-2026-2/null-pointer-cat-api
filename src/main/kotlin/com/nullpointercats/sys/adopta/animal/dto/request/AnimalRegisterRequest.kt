package com.nullpointercats.sys.adopta.animal.dto.request

import com.nullpointercats.sys.adopta.animal.domain.Breed
import com.nullpointercats.sys.adopta.animal.domain.Photo
import com.nullpointercats.sys.adopta.user.domain.User
import java.time.LocalDate
import java.time.LocalDateTime

data class AnimalRegisterRequest (
    var animalName : String,

    var species : String,

    val dateOfBirth : LocalDate ? = null,

    val description: String ? = null,

    val size: String ? = null,

    val animalZipcode: String,

    val publishedAt: LocalDateTime? = LocalDateTime.now(),

    val breedId: Int? = null,

    val photosURLs: List<String>

)