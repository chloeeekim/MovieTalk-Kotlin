package chloe.movietalk.domain

import chloe.movietalk.domain.enums.Gender
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany
import lombok.*
import java.util.function.Function

@ToString
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class Actor @Builder constructor(name: String, gender: Gender, country: String) : Person(name, gender, country) {
    @OneToMany(mappedBy = "actor", cascade = [CascadeType.ALL], orphanRemoval = true)
    private var movieActors: MutableList<MovieActor?> = ArrayList<MovieActor?>()

    fun updateActor(actor: Actor) {
        super.updatePerson(actor.name, actor.gender, actor.country)
        this.movieActors = actor.getMovieActors()
    }

    val movies: MutableList<Movie?>
        get() = movieActors.stream().map<Movie?>((Function { obj: MovieActor? -> obj!!.getMovie() }))
            .toList()

    fun addMovie(movie: Movie) {
        val movieActor = MovieActor(movie, this)
        movieActors.add(movieActor)
        movie.getMovieActors().add(movieActor)
    }
}
