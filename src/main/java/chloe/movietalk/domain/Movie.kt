package chloe.movietalk.domain

import jakarta.persistence.*
import lombok.*
import java.time.LocalDate
import java.util.*

@ToString
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class Movie @Builder constructor(
    @field:Column(
        name = "code_fims",
        length = 50,
        nullable = false
    ) private var codeFIMS: String?,
    @field:Column(nullable = false) private var title: String?,
    @field:Column(columnDefinition = "TEXT") private var synopsis: String?,
    @field:Column(name = "release_date") private var releaseDate: LocalDate?,
    @field:Column(name = "prod_year") private var prodYear: Int?,
    @field:JoinColumn(name = "director_id") @field:ManyToOne(fetch = FetchType.LAZY) private var director: Director?,
    totalRating: Double?,
    reviewCount: Int?
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private var id: UUID? = null

    @OneToMany(mappedBy = "movie", cascade = [CascadeType.ALL], orphanRemoval = true)
    private var movieActors: MutableList<MovieActor?> = ArrayList<MovieActor?>()

    @OneToMany(mappedBy = "movie", cascade = [CascadeType.ALL], orphanRemoval = true)
    private var reviews: MutableList<Review?>? = ArrayList<Review?>()

    private var totalRating = 0.0

    private var reviewCount = 0

    init {
        this.totalRating = if (totalRating != null) totalRating else 0.0
        this.reviewCount = if (reviewCount != null) reviewCount else 0
    }

    fun updateMovie(movie: Movie) {
        this.codeFIMS = movie.getCodeFIMS()
        this.title = movie.getTitle()
        this.synopsis = movie.getSynopsis()
        this.releaseDate = movie.getReleaseDate()
        this.prodYear = movie.getProdYear()
        this.director = movie.getDirector()
        this.movieActors = movie.getMovieActors()
        this.reviews = movie.getReviews()
        this.totalRating = movie.getTotalRating()
        this.reviewCount = movie.getReviewCount()
    }

    val actors: MutableList<Actor?>
        get() = movieActors.stream().map<Actor?> { obj: MovieActor? -> obj!!.getActor() }.toList()

    fun addActor(actor: Actor) {
        val movieActor = MovieActor(this, actor)
        movieActors.add(movieActor)
        actor.getMovieActors().add(movieActor)
    }

    fun changeDirector(newDirector: Director?) {
        if (this.director != null) {
            this.director!!.getFilmography().remove(this)
        }
        this.director = newDirector
        if (newDirector != null && !newDirector.getFilmography().contains(this)) {
            newDirector.getFilmography().add(this)
        }
    }

    fun removeDirector() {
        this.director = null
    }

    val averageRating: Double
        get() = if (reviewCount > 0) Math.round(totalRating / reviewCount * 10.0) / 10.0 else 0.0

    fun updateTotalRating(totalRating: Double) {
        this.totalRating = totalRating
    }

    fun updateReviewCount(reviewCount: Int) {
        this.reviewCount = reviewCount
    }
}