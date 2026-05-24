package com.nullpointercats.sys.adopta.animal.dto.external

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ExternalBreedDto(
    val id: Int,
    val name: String,
    val origin: String? = null,
    val temperament: String? = null,
    @JsonProperty("life_span")
    val lifeSpan: String? = null
)