package com.nullpointercats.sys.adopta.post.dto.response

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * DTO representing a single post entry in the public adoption feed.
 *
 * Combines data from both the [Post] and its associated [Animal],
 * so the client can render a complete feed card in a single response.
 */

data class PostFeedResponse(
    val idPost: Int,
    val description: String?,
    val status: String,
    val createdAt: LocalDateTime,

    val publisherUsername: String,
    val publisherFirstname: String,
    val publisherLastname: String,
    val publisherId: Int,

    val idAnimal: Int,
    val animalName: String,
    val species: String,
    val size: String?,
    val dateOfBirth: LocalDate?,
    val animalZipcode: String,
    val breedName: String?,
    val animalDescription: String?,
    val photos: List<String>,
)