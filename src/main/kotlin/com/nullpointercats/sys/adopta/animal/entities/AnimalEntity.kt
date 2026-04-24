package com.nullpointercats.sys.adopta.animal.entities

import com.nullpointercats.sys.adopta.user.entities.UserEntity
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * AnimalEntity is a persistence entity that represents an animal in the system.
 * This class maps to the apoption_animal table.
 * */
@Entity
@Table(name = "animals")
data class AnimalEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_animal", nullable = false)
    val idAnimal: Int = 0,

    @Column(name = "animal_name", nullable = false)
    var animalName: String,

    @Column(name = "species", nullable = false)
    var species: String,

    @Column(name = "date_of_birth")
    var dateOfBirth: LocalDate? = null,

    @Column(name = "description")
    var description: String? = null,

    @Column(name = "size")
    var size: String ? = null,

    @Column(name = "animal_zipcode", nullable = false)
    var animalZipcode: String,

    @Column(name = "published_at")
    var publishedAt: LocalDateTime = LocalDateTime.now(),

    //Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    var user: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_breed")
    var breed: BreedEntity? = null,

    @OneToMany(mappedBy = "animal" , cascade = [CascadeType.ALL], orphanRemoval = true) // Not sure of this configuration
    var photos: MutableList<PhotoEntity> = mutableListOf(),
    )