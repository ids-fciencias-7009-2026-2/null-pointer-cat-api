package com.nullpointercats.sys.adopta.animal.services

import com.nullpointercats.sys.adopta.animal.dto.external.ExternalBreedDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class ExternalPetApiClient {

    @Value("\${external.api.dog.url}")
    private lateinit var dogApiUrl: String

    @Value("\${external.api.dog.key}")
    private lateinit var dogApiKey: String

    @Value("\${external.api.cat.url}")
    private lateinit var catApiUrl: String

    @Value("\${external.api.cat.key}")
    private lateinit var catApiKey: String

    private val restTemplate = RestTemplate()

    fun fetchBreedsFromExternalApi(species: String): List<ExternalBreedDto> {
        val (url, apiKey) = when (species.uppercase()) {
            "DOG" -> Pair(dogApiUrl, dogApiKey)
            "CAT" -> Pair(catApiUrl, catApiKey)
            else -> return emptyList()
        }

        val finalUrl = "$url/breeds"

        return try {
            val headers = HttpHeaders()
            headers.set("x-api-key", apiKey)
            headers.accept = listOf(MediaType.APPLICATION_JSON)

            val entity = HttpEntity<String>(headers)
            val responseType = object : ParameterizedTypeReference<List<ExternalBreedDto>>() {}

            val response = restTemplate.exchange(finalUrl, HttpMethod.GET, entity, responseType)

            response.body ?: emptyList()
        } catch (e: Exception) {
            println("ERROR FETCHING EXTERNAL BREEDS FOR $species: ${e.message}")
            emptyList()
        }
    }
}