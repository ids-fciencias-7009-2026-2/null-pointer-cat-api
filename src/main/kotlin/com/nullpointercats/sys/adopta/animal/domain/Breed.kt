package com.nullpointercats.sys.adopta.animal.domain

/**
 * Represents a breed catalogue entry for dogs or cats in the Adopta system.
 */
data class Breed (

    val idBreed: Int,

    val breedName : String,

    val origin : String ? = null,

    val temperament: String ? = null,

    val lifeSpan : String ? = null,
)