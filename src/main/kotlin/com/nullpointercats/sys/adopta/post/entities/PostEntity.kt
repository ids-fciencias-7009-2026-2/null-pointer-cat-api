package com.nullpointercats.sys.adopta.post.entities

import com.nullpointercats.sys.adopta.animal.entities.AnimalEntity
import jakarta.persistence.*
import jakarta.persistence.GenerationType
import java.time.LocalDateTime

@Entity
@Table(name = "post")
data class PostEntity (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_post", nullable = false)
    val idPost: Int = 0,

    @Column(name = "description")
    var description: String? = null,

    @Column(name = "status")
    var status: String? = null,

    @Column(name = "created_at" )
    val createdAt: LocalDateTime = LocalDateTime.now(),


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_animal", nullable = false)
    var animal: AnimalEntity

    )