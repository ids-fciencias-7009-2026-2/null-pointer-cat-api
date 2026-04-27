package com.nullpointercats.sys.adopta.animal.domain

/**
 * Represents a breed catalogue entry for dogs or cats in the Adopta system.
 */
data class Breed (
    /**
     * Unique identifier of the breed.
     * */
    val idBreed: Int ? = null,

    /**
     * Name of the breed.
     * */
    val breedName : String,

    /**
     * County or regin of origin of the breed.
     * */
    val origin : String ? = null,

    /**
     * Brief description of the behavior of the breed.
     * */
    val temperament: String ? = null,

    /**
     * Average life span of the breed.
     * */
    val lifeSpan : String ? = null,
)