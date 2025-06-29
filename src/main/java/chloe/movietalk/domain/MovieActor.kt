package chloe.movietalk.domain

import jakarta.persistence.*
import lombok.AccessLevel
import lombok.Getter
import lombok.NoArgsConstructor

@Entity
@Getter
@Table(name = "MOVIE_ACTOR")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class MovieActor(
    @field:JoinColumn(name = "movie_id") @field:ManyToOne(fetch = FetchType.LAZY) private var movie: Movie?,
    @field:JoinColumn(
        name = "actor_id"
    ) @field:ManyToOne(fetch = FetchType.LAZY) private var actor: Actor?
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null
}
