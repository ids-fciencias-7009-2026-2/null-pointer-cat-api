package com.nullpointercats.sys.adopta.animal.domain

/**
 * Represents a photograph associated with an animal.
 */
data class Photo (

    var idPhoto: Int,

    var url: String,

    var width: Int ? = null,

    var height: Int ? = null,
)