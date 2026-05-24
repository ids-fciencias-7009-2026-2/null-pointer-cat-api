package com.nullpointercats.sys.adopta.geocoding.services

import com.nullpointercats.sys.adopta.geocoding.dto.GeocodingResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

/**
 * Service responsible for resolving a Mexican postal code (CP)
 * into geographic coordinates using the Mapbox Geocoding API.
 *
 * The Mapbox access token is injected from the application
 * properties and must be set via the MAPBOX_TOKEN environment variable.
 */
@Service
class GeocodingService {

    @Value("\${mapbox.token}")
    lateinit var mapboxToken: String

    fun getCoordinatesByZipcode(zipcode: String): GeocodingResponse? {
        val url = "https://api.mapbox.com/geocoding/v5/mapbox.places/$zipcode.json?country=mx&access_token=$mapboxToken"
        val restTemplate = RestTemplate()

        val response = restTemplate.getForObject(url, Map::class.java) ?: return null

        @Suppress("UNCHECKED_CAST")
        val features = response["features"] as? List<Map<String, Any>> ?: return null
        if (features.isEmpty()) return null

        @Suppress("UNCHECKED_CAST")
        val center = features[0]["center"] as? List<Double> ?: return null

        return GeocodingResponse(lat = center[1], lng = center[0])
    }
}