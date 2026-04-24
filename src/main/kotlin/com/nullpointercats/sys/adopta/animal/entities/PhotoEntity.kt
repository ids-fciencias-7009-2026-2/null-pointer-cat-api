package com.nullpointercats.sys.adopta.animal.entities

import jakarta.persistence.*

@Entity
@Table(name = "photo")
data class PhotoEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_photo")
    var idPhoto : Int = 0,

    @Column(name = "url", nullable = false)
    var url : String,

    @Column(name = "width")
    var width : Int ? = null,

    @Column(name = "height")
    var height : Int ? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_animal", nullable = false)
    var animal : AnimalEntity,
)