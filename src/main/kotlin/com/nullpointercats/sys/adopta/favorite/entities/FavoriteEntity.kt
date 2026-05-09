package com.nullpointercats.sys.adopta.favorite.entities

import com.nullpointercats.sys.adopta.animal.entities.AnimalEntity
import com.nullpointercats.sys.adopta.user.entities.UserEntity
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime

data class FavoriteId(
    val user: Int? = null,
    val animal: Int? = null
) : Serializable

@Entity
@Table(name = "animal_favorite")
@IdClass(FavoriteId::class)
data class FavoriteEntity(

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    var user: UserEntity,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_animal", nullable = false)
    var animal: AnimalEntity,

    @Column(name = "saved_at")
    val savedAt: LocalDateTime = LocalDateTime.now()
)
