import com.nullpointercats.sys.adopta.animal.domain.Breed
import com.nullpointercats.sys.adopta.animal.entities.BreedEntity

fun Breed.toEntity() : BreedEntity {
    return BreedEntity(
        idBreed = this.idBreed ?: 0,
        breedName = this.breedName,
        origin = this.origin,
        temperament = this.temperament,
        lifeSpan = this.lifeSpan,
    )
}