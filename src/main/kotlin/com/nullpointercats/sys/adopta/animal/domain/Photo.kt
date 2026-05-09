package com.nullpointercats.sys.adopta.animal.domain

/**
 * Represents a photograph associated with an animal.
 */

data class Photo (
    /**
     * Unique identifier of the photo.
     * */
    var idPhoto: Int? = null,

    /**
     * URL of the photo.
     * */
    var url: String,

    /**
     * Image width in pixels.
     * */
    var width: Int? = null,

    /**
     * Image height in pixels.
     * */
    var height: Int? = null,
)