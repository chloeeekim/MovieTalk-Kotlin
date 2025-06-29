package chloe.movietalk.domain

import jakarta.persistence.*

@Entity
@Table(name = "MOVIE_ACTOR")
class MovieActor(
    @JoinColumn(name = "movie_id")
    @ManyToOne(fetch = FetchType.LAZY)
    var movie: Movie,

    @JoinColumn(name = "actor_id")
    @ManyToOne(fetch = FetchType.LAZY)
    var actor: Actor
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
