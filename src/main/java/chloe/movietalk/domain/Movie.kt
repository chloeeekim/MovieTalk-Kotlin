package chloe.movietalk.domain

import jakarta.persistence.*
import java.time.LocalDate
import java.util.*
import kotlin.math.roundToInt

@Entity
class Movie(
    @Column(name = "code_fims", length = 50, nullable = false)
    var codeFIMS: String,

    @Column(nullable = false)
    var title: String,

    @Column(columnDefinition = "TEXT")
    var synopsis: String,

    @Column(name = "release_date")
    var releaseDate: LocalDate,

    @Column(name = "prod_year")
    var prodYear: Int,

    @JoinColumn(name = "director_id")
    @ManyToOne(fetch = FetchType.LAZY)
    var director: Director? = null
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true, updatable = false, columnDefinition = "BINARY(16)")
    val id: UUID? = null

    @OneToMany(mappedBy = "movie", cascade = [CascadeType.ALL], orphanRemoval = true)
    var movieActors: MutableList<MovieActor> = mutableListOf()

    @OneToMany(mappedBy = "movie", cascade = [CascadeType.ALL], orphanRemoval = true)
    var reviews: MutableList<Review> = mutableListOf()

    var totalRating: Double = 0.0

    var reviewCount: Int = 0

    fun updateMovie(movie: Movie) {
        this.codeFIMS = movie.codeFIMS
        this.title = movie.title
        this.synopsis = movie.synopsis
        this.releaseDate = movie.releaseDate
        this.prodYear = movie.prodYear
        this.director = movie.director
        this.totalRating = movie.totalRating
        this.reviewCount = movie.reviewCount

        this.movieActors.clear()
        this.movieActors.addAll(movie.movieActors)

        this.reviews.clear()
        this.reviews.addAll(movie.reviews)
    }

    fun getActors() : List<Actor> {
        return movieActors.map { it.actor }
    }

    fun addActor(actor: Actor) {
        val movieActor = MovieActor(this, actor)
        movieActors.add(movieActor)
        actor.movieActors.add(movieActor)
    }

    fun changeDirector(newDirector: Director) {
        this.director?.filmography?.remove(this)

        this.director = newDirector
        if (newDirector != null && !newDirector.filmography.contains(this)) {
            newDirector.filmography.add(this)
        }
    }

    fun removeDirector() {
        this.director = null
    }

    fun getAverageRating() : Double {
        return if (reviewCount > 0) (totalRating / reviewCount * 10.0).roundToInt() / 10.0 else 0.0
    }

    fun updateTotalRating(totalRating: Double) {
        this.totalRating = totalRating
    }

    fun updateReviewCount(reviewCount: Int) {
        this.reviewCount = reviewCount
    }
}