package chloe.movietalk.domain

import chloe.movietalk.domain.enums.Gender
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany

@Entity
class Actor(
    name: String,
    gender: Gender,
    country: String
) : Person(name, gender, country) {
    @OneToMany(mappedBy = "actor", cascade = [CascadeType.ALL], orphanRemoval = true)
    var movieActors: MutableList<MovieActor> = mutableListOf()

    fun updateActor(actor: Actor) {
        super.updatePerson(actor.name, actor.gender, actor.country)
        this.movieActors.clear()
        this.movieActors.addAll(actor.movieActors)
    }

    fun getMovies() : List<Movie> {
        return movieActors.map { it.movie }
    }

    fun addMovie(movie: Movie) {
        val movieActor = MovieActor(movie, this)
        movieActors.add(movieActor)
        movie.movieActors.add(movieActor)
    }
}
