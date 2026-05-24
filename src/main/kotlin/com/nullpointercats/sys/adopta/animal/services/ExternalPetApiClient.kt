package com.nullpointercats.sys.adopta.animal.services

import com.nullpointercats.sys.adopta.animal.dto.external.ExternalBreedDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
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
        val (url, key) = when (species.uppercase()) {
            "DOG" -> Pair("${dogApiUrl}/breeds", dogApiKey) // Usa llaves de interpolación seguras
            "CAT" -> Pair("${catApiUrl}/breeds", catApiKey)
            else -> return emptyList()
        }

        val headers = HttpHeaders()
        // Forma obligatoria en la que la API pide la llave.
        headers.set("x-api-key", key)
        val entity = HttpEntity<Void>(headers)

        return try {
            val responseType = object : ParameterizedTypeReference<List<ExternalBreedDto>>() {}
            val response = restTemplate.exchange(url, HttpMethod.GET, entity, responseType)
            response.body ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}